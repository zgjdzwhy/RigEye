package com.rigai.rigeye.common.kafka;


import com.alibaba.fastjson.JSON;
import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.common.kafka.consumer.Consumer;
import com.rigai.rigeye.common.kafka.consumer.ConsumerFactory;
import com.rigai.rigeye.common.kafka.consumer.impl.ConsumerImpl;
import com.rigai.rigeye.common.kafka.producer.Producer;
import com.rigai.rigeye.common.kafka.producer.ProducerFactory;
import com.rigai.rigeye.common.kafka.producer.impl.ProducerImpl;
import com.em.fx.redis.dao.RedisDao;
import com.google.common.collect.ImmutableMap;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.BrokerEndPoint;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/10.
 */

@Component
public class KafkaUtils {
    @Autowired
    RedisDao redisDao;

    /**
     * 用于数据源测试，由于只是用于测试时短暂发送一个消息，生产者将会在发送消息后被销毁
     * @param service A list of host/port pairs to use for establishing the initial connection to the Kafka cluster
     * @param topic A name topic the message send to
     */
    public void dataSourceTestSend(String service,String topic,String message){
        Producer producer=new ProducerImpl();
        KafkaProducer<String,String> kafkaProduce=ProducerFactory.simpleCreateProducer(service);
        producer.init(kafkaProduce);
        producer.asyncSend(topic,message);
        producer.destroy(3L, TimeUnit.SECONDS);
    }


    public void dataSourceTestConsumer(String service,String topic,long poll,String groupId){
        KafkaConsumer<String,String> kafkaConsumer= ConsumerFactory.simpleCreateConsumer(service,groupId, false);
        Consumer consumer=new ConsumerImpl();
        consumer.init(kafkaConsumer);
        consumer.consumer(topic,poll);
        consumer.destroy();
    }

    public List<String> readKafka(String brokers, Collection<String> topics, String group){
        //读取上次offset
        Map<TopicPartition, Long> topicPartitionLongMap = readOffsetsFromRedis(brokers,topics, group);

        //获取新消息
        KafkaConsumer<String, String> kafkaConsumer = ConsumerFactory.simpleCreateConsumer(brokers, group, false);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitionLongMap.forEach((topicPartition, aLong) -> topicPartitions.add(topicPartition));
        kafkaConsumer.assign(topicPartitions);
        for (Map.Entry<TopicPartition,Long> entry : topicPartitionLongMap.entrySet()){
            kafkaConsumer.seek(entry.getKey(),entry.getValue());
        }
        ConsumerRecords<String,String> records = kafkaConsumer.poll(100);

        List<String> logs = new ArrayList<>();
        for (ConsumerRecord<String, String> record : records) {
            if (logs.size() < 10){
                logs.add(record.value());
            }
        }
        kafkaConsumer.close();
        return logs;
    }


    /**
     * 获取topic 分区和offset
     * @param brokers
     * @param topics
     * @return
     */
    public Map<TopicPartition, Long> readOffsetsFromRedis(String brokers, Collection<String> topics, String group){
        //读取kafka最新partition offset
        Map<TopicPartition, Long> kafkaOffset = new HashMap<>(8);
        for (String topic : topics) {
            Map<TopicAndPartition, Long> offsets = getTopicOffsetsMap(brokers, topic);
            offsets.forEach((topicAndPartition, aLong) -> {
                kafkaOffset.put(new TopicPartition(topicAndPartition.topic(),topicAndPartition.partition()), aLong);
            });
        }

        String offsetsStr = redisDao.getStr(CommonConstant.GROUP_PREFIX+group);
        if (offsetsStr == null){
            Map<TopicPartition, Long> kafkaOffsetNew = new HashMap<>(8);
            //redis中无消费记录，直接从头取数据
            kafkaOffset.forEach((topicPartition, aLong) -> {
                kafkaOffsetNew.put(topicPartition,1L);
            });
            return kafkaOffsetNew;
        }else {
            //读取redis中的offset
            Map<String,String> redisOffsets = (Map<String, String>) JSON.parse(offsetsStr);
            Map<TopicPartition, Long> res = new HashMap<>(8);
            for (Map.Entry<String,String> entry:redisOffsets.entrySet()){
                Map<String, Integer> map = (Map) JSON.parse(entry.getKey());
                for (Map.Entry<String, Integer> topicAndPartition: map.entrySet()){
                    res.put(new TopicPartition(topicAndPartition.getKey(),topicAndPartition.getValue()), Long.valueOf(entry.getValue()));
                    break;
                }
            }
            //对比更新offset
            kafkaOffset.forEach((topicPartition, aLong) -> {
                if (!res.containsKey(topicPartition)){
                    res.put(topicPartition,0L);
                }
            });
            return res;
        }
    }

    private Map<TopicAndPartition, Long> getTopicOffsetsMap(String brokers, String topic) {
        Map<TopicAndPartition, Long> retVals = new HashMap<TopicAndPartition, Long>(8);
        for (String zkserver : brokers.split(",")) {
            SimpleConsumer simpleConsumer = new SimpleConsumer(zkserver.split(":")[0],
                    Integer.valueOf(zkserver.split(":")[1]), 100000, 1024,
                    "consumser");
            TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(Arrays.asList(topic));

            TopicMetadataResponse topicMetadataResponse = simpleConsumer.send(topicMetadataRequest);

            for (TopicMetadata metadata : topicMetadataResponse.topicsMetadata()) {
                for (PartitionMetadata part : metadata.partitionsMetadata()) {
                    BrokerEndPoint leader =  part.leader();
                    if (leader != null) {
                        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, part.partitionId());
                        PartitionOffsetRequestInfo partitionOffsetRequestInfo = new PartitionOffsetRequestInfo(
                                kafka.api.OffsetRequest.LatestTime(), 10000);
                        OffsetRequest offsetRequest = new OffsetRequest(
                                ImmutableMap.of(topicAndPartition, partitionOffsetRequestInfo),
                                kafka.api.OffsetRequest.CurrentVersion(), simpleConsumer.clientId());
                        OffsetResponse offsetResponse = simpleConsumer.getOffsetsBefore(offsetRequest);
                        if (!offsetResponse.hasError()) {
                            long[] offsets = offsetResponse.offsets(topic, part.partitionId());
                            retVals.put(topicAndPartition, offsets[0]);
                        }
                    }
                }
            }
            simpleConsumer.close();
        }
        return retVals;
    }
}
