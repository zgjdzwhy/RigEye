package com.rigai.rigeye;

import com.rigai.rigeye.common.ApolloConfig;
import com.rigai.rigeye.common.BaseConstant;
import com.rigai.rigeye.model.DataTask;
import com.rigai.rigeye.model.ExceptionConfig;
import com.rigai.rigeye.service.impl.DataLandingServiceImpl;
import com.rigai.rigeye.service.impl.DataProcServiceImpl;
import com.rigai.rigeye.service.impl.ExceptionCheckServiceImpl;
import com.rigai.rigeye.util.KafkaOffsetUtil;
import com.rigai.rigeye.util.ValidatorResult;
import com.rigai.rigeye.util.ValidatorUtil;
import com.em.fx.blockly.xmlsplit.BlocklyXmlParseLog;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.serializer.KryoRegistrator;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author yh
 * @date 2018/8/16 17:58
 */
public class App{
    private static final Logger logger= LoggerFactory.getLogger(App.class);

    /**
     * 自定义序列化类
     */
    public static class toKryoRegistrator implements KryoRegistrator {
        @Override
        public void registerClasses(Kryo kryo) {
            //在Kryo序列化库中注册自定义的类
            kryo.register(DataLandingServiceImpl.class, new FieldSerializer(kryo, DataLandingServiceImpl.class));
            //在Kryo序列化库中注册自定义的类
            kryo.register(DataProcServiceImpl.class, new FieldSerializer(kryo, DataProcServiceImpl.class));
            kryo.register(ApolloConfig.class, new FieldSerializer(kryo, ApolloConfig.class));
        }
    }

    public static void main(String[] args){
        if (args.length < 0){
            logger.error("任务id不能为空！");
        }
        String id = args[0];
        //获取任务
        DataTask dataTask = DataProcServiceImpl.INSTANCE.getOne(Long.parseLong(id));
        //校验任务
        ValidatorResult validatorResult = ValidatorUtil.validate(dataTask);
        if (validatorResult.isHasErrors()){
            logger.error("任务参数错误{}",validatorResult);
            return;
        }

        //创建influx db数据库
        InfluxDB influxDB = InfluxDBFactory.connect(ApolloConfig.INSTANCE.getInfluxUrl(), ApolloConfig.INSTANCE.getInfluxUser(), ApolloConfig.INSTANCE.getInfluxPass());
        if (!influxDB.databaseExists(ApolloConfig.INSTANCE.getInfluxDb())) {
            influxDB.createDatabase(ApolloConfig.INSTANCE.getInfluxDb());
        }
        //关闭连接
        influxDB.close();

        //spark任务参数
        SparkSession sparkSession = SparkSession.builder()
//                .appName("aa").master("local[2]")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.kryo.registrator", toKryoRegistrator.class.getName())
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());
        sc.setLogLevel("WARN");
        JavaStreamingContext jsc = new JavaStreamingContext(sc, Durations.seconds(10));
        Map<String, Object> kafkaParams = new HashMap<>(16);
        //kafka参数
        String brokers = dataTask.getDataSourceDTO().getClusterIp();
        String groupId = dataTask.getDataSourceDTO().getConsumerGroup();
        String topic = dataTask.getDataSourceDTO().getTopic();
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", groupId);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        kafkaParams.put("max.poll.records", ApolloConfig.INSTANCE.getKafkaMaxPollRecords());
        Collection<String> topics = Arrays.asList(topic.split(","));

        //切分正常的日志表schema
        StructType schema = DataProcServiceImpl.INSTANCE.createSchema(dataTask);
        //切分异常的日志表schema
        StructType exceptSchema = DataProcServiceImpl.INSTANCE.exceptSchema();

        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                jsc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams, KafkaOffsetUtil.INSTANCE.readOffsetsFromRedis(brokers, topics, groupId))
        );

        //初始化时获取redis配置信息,然后广播参数
        ExceptionConfig exceptionConfig = ExceptionCheckServiceImpl.INSTANCE.getExceptionConfig();

        //广播配置
        final Broadcast<ExceptionConfig> exceptionConfigBroadcast = new JavaSparkContext(sparkSession.sparkContext()).broadcast(exceptionConfig);

        //原始日志
        JavaDStream<ConsumerRecord<String, String>> origin = stream.transform(v1 -> {
            OffsetRange[] offsetRanges = ((HasOffsetRanges) v1.rdd()).offsetRanges();
            try{
                //记录offset
                KafkaOffsetUtil.INSTANCE.saveOffsetsToRedis(groupId, offsetRanges);
            }catch (Exception e){
                System.out.println("error "+e);
            }
            return v1;
        });

        //对原始日志切分，获取切分正常的日志
        JavaDStream<Row> goodRow = origin.mapPartitions((FlatMapFunction<Iterator<ConsumerRecord<String, String>>, Row>) consumerRecordIterator -> {
            List<Row> rows = new ArrayList<>();
            try{
                BlocklyXmlParseLog blocklyXmlParseLog = new BlocklyXmlParseLog(dataTask.getLogAnalysisXml());
                while (consumerRecordIterator.hasNext()){
                    String value = consumerRecordIterator.next().value();
                    Map map = blocklyXmlParseLog.parseLog(value);
                    Integer status = (Integer) map.get("status");
                        if (status == 200) {
                            List<Map> list = (List<Map>) map.get("data");
                            Row row = DataProcServiceImpl.INSTANCE.parseLog(exceptionConfigBroadcast.getValue(), value, dataTask, list);
                            rows.add(row);
                        }
                }
            } catch (Exception e) {
                System.out.println("divide error." + e);
                logger.error("divide error." + e);
            }
            return rows.iterator();
        }).filter(Objects::nonNull);

        goodRow.cache();

        //对原始日志切分，获取切分异常的的日志.切分错误的日志固定3个字段：_sysTime, _line, message
        JavaDStream<Row> badRow = origin.mapPartitions(consumerRecordIterator -> {
            List<Row> rows = new ArrayList<>();
            //清洗日志
            BlocklyXmlParseLog blocklyXmlParseLog = new BlocklyXmlParseLog(dataTask.getLogAnalysisXml());
            while (consumerRecordIterator.hasNext()) {
                String value = consumerRecordIterator.next().value();
                Map map = blocklyXmlParseLog.parseLog(value);
                Integer status = (Integer) map.get("status");
                try {
                    if (status != 200) {
                        List<Map> list = (List<Map>) map.get("data");
                        String message = (String) map.get("message");
                        Row row = DataProcServiceImpl.INSTANCE.parseBadLog(list, message);
                        rows.add(row);
                    }
                } catch (Exception e) {
                    System.out.println("divide bad error." + e);
                    logger.error("divide bad error." + e);
                }
            }
            return rows.iterator();
        }).filter(Objects::nonNull);

        badRow.cache();

        //切分异常的日志聚合
        badRow.window(Durations.seconds(BaseConstant.WINDOW_TIME),Durations.seconds(BaseConstant.WINDOW_TIME)).foreachRDD(javaRDD -> {
            try{
                DataProcServiceImpl.INSTANCE.aggBad(sparkSession,javaRDD,exceptSchema, dataTask);
            }catch (Exception e){
                System.out.println("error "+e);
            }
        });

        //切分正确的日志聚合，统计总数写入influxDB
        goodRow.window(Durations.seconds(BaseConstant.WINDOW_TIME),Durations.seconds(BaseConstant.WINDOW_TIME)).foreachRDD(javaRDD -> {
            try{
                DataProcServiceImpl.INSTANCE.aggGood(sparkSession,javaRDD,schema,dataTask);
            }catch (Exception e){
                System.out.println("error "+e);
            }
        });

        // Start the computation
        jsc.start();
        try {
            // Wait for the computation to terminate
            jsc.awaitTermination();
        } catch (InterruptedException e) {
            System.out.println("error "+e);
        }
    }
}
