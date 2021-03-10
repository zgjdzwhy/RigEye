package com.rigai.rigeye.service.impl;

import com.alibaba.fastjson.JSON;
import com.rigai.rigeye.common.BaseConstant;
import com.rigai.rigeye.dto.Filter;
import com.rigai.rigeye.dto.Measure;
import com.rigai.rigeye.model.DataTask;
import com.rigai.rigeye.model.DivideModel;
import com.rigai.rigeye.model.ExceptionConfig;
import com.rigai.rigeye.service.DataProcService;
import com.rigai.rigeye.util.ElasticUtil;
import com.rigai.rigeye.util.JedisPoolUtil;
import com.rigai.rigeye.util.ValidatorResult;
import com.rigai.rigeye.util.ValidatorUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.rigai.rigeye.common.BaseConstant.*;
import static org.apache.spark.sql.functions.*;

/**
 * @author yh
 * @date 2018/8/20 15:36
 */
public enum  DataProcServiceImpl implements DataProcService,Serializable{
    INSTANCE;
    private static final Logger logger= LoggerFactory.getLogger(DataProcServiceImpl.class);
    @Override
    public DataTask getOne(Long id) {
        String key = PREFIX_DATA_TASK + id;
        try (Jedis jedis = JedisPoolUtil.INSTANCE.getResource()) {
            String value = jedis.get(key);
            if (StringUtils.isNotEmpty(value)) {
                return JSON.parseObject(value, DataTask.class);
            }
        } catch (Exception e) {
            System.out.println("error "+e);
        }
        return null;
    }

    @Override
    public Row parseLog(ExceptionConfig exceptionConfig, String log, DataTask dataTask, List<Map> list) {
        List<DivideModel> models = dataTask.getModel();
        Object[] o = new Object[models.size()];
        List<String> names = models.parallelStream().map(DivideModel::getName).collect(Collectors.toList());
        //记录下异常数据集_error_key 需要的字段,_error_key是由_line匹配异常模板得到的。
        int p = -1;
        //添加监控项目名称
        int q = -1;
        for (int i = 0; i < names.size(); i++) {
            if ("_error_key".equals(names.get(i))){
                p = i;
            }
            if ("_appName".equals(names.get(i))){
                q = i;
            }
            for (Map map1 : list) {
                String name = (String) map1.get("name");
                Object value = map1.get("value");
                String type = (String) map1.get("type");

                if (names.get(i).equals(name)) {
                    if ("date".equals(type)) {
                        o[i] = new Timestamp((Long) value);
                    } else if ("long".equals(type)){
                        o[i] = Long.parseLong(value.toString());
                    }else {
                        o[i] = value;
                    }
                    break;
                }
            }
        }

        if (q > -1){
            o[q] = dataTask.getAppName();
        }

        //使用原始日志查找异常模板,
        try{
            String errorKey = ExceptionCheckServiceImpl.INSTANCE.getErrorKey(exceptionConfig,log, dataTask);
            if (p > -1) {
                o[p] = errorKey;
            }
        }catch (Exception e){
            System.out.println("get errorKey exception."+e);
           logger.info("get errorKey exception.{0}",e);
        }
        return RowFactory.create(o);
    }

    @Override
    public Row parseBadLog(List<Map> list,String message){
        Object[] o = new Object[EXCEPTION_FIELD_NUM];
        for (Map data : list) {
            String name = (String) data.get("name");
            Object value = data.get("value");
            if ("_sysTime".equals(name)) {
                //时间精确到秒
                o[0] = new Timestamp((Long) value);
            }else if ("_line".equals(name)){
                o[1] = value;
            }
        }
        o[2] = message;
        return RowFactory.create(o);
    }

    @Override
    public StructType createSchema(DataTask dataTask) {
        List<StructField> fields = new ArrayList<>();
        dataTask.getModel().forEach(model -> {
            switch (model.getType()){
                case "date":
                    fields.add(DataTypes.createStructField(model.getName(), DataTypes.TimestampType, true));
                    break;
                case "string":
                    fields.add(DataTypes.createStructField(model.getName(), DataTypes.StringType, true));
                    break;
                case "long":
                    fields.add(DataTypes.createStructField(model.getName(), DataTypes.LongType, true));
                    break;
                case "double":
                    fields.add(DataTypes.createStructField(model.getName(), DataTypes.DoubleType, true));
                    break;
                    default:break;
            }
        });
        return DataTypes.createStructType(fields);
    }

    @Override
    public StructType exceptSchema() {
        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("_sysTime", DataTypes.TimestampType, true));
        fields.add(DataTypes.createStructField("_line", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("message", DataTypes.StringType, true));
        return DataTypes.createStructType(fields);
    }

    public StringBuilder filterCondition(List<Filter> filters, String filterOperator){
        StringBuilder condition = new StringBuilder();

        for (int i = 0; i< filters.size(); i++){
            Filter filter = filters.get(i);
            String ex = filter.getExpression();
            if ("==".equals(ex)){
                condition.append("`").append(filter.getField()).append("` ").append(" = '").append(filter.getValue()).append("'");
            }else if ("包含".equals(ex)){
                condition.append("`").append(filter.getField()).append("` like '%").append(filter.getValue()).append("'%");
            }else if ("不包含".equals(ex)){
                condition.append("`").append(filter.getField()).append("` not like '%").append(filter.getValue()).append("'%");
            }else if ("IS NULL".equals(ex)){
                condition.append("`").append(filter.getField()).append("` ").append(ex);
            }else if ("NOT NULL".equals(ex)){
                //condition.append("`").append(filter.getField()).append("` IS ").append(ex);
                condition.append("`").append(filter.getField()).append("` ").append(" != '").append("'");
            }else {
                condition.append("`").append(filter.getField()).append("` ").append(ex).append(" '").append(filter.getValue()).append("'");
            }

            if (i != filters.size() - 1){
                if (filterOperator.equals(AND)) {
                    condition.append(" and ");
                }else {
                    condition.append(" or ");
                }
            }
        }
        return condition;
    }

    public StringBuilder measureCondition(List<Measure> measures){
        StringBuilder stringBuilder = new StringBuilder("select ");
        measures.forEach(measure -> stringBuilder.append(measure.getAggregates().get(0))
                .append("(`").append(measure.getMeasureA()).append("`)")
                .append(" as `").append(measure.getMeasureLabel()).append("`, "));
        return stringBuilder;
    }

    @Override
    public void aggGood(SparkSession sparkSession, JavaRDD<Row> javaRDD, StructType schema, DataTask dataTask) {
        //构造表
        Dataset<Row> table = sparkSession.createDataFrame(javaRDD, schema);
        table.cache();
        //是否有数据
        boolean f = table.select(LINE).limit(1).count() > 0;
        if (f){
            //正确切分的数据，统计数量写入influx
            Dataset<Row> goodCount = table
                    .withColumn(SYS_TIME,date_format(col(SYS_TIME),"yyyy-MM-dd HH:mm").cast(DataTypes.TimestampType))
                    .groupBy(col("_sysTime")).agg(count("_line").as("total"));
            goodCount.cache();
            DataLandingServiceImpl.INSTANCE.writeDivideCount(goodCount,INFLUX_DIVIDE_DATA_SET, dataTask.getId(), true);

            //日志清洗明细存入ES。筛选异常关键字不为空的记录
            Dataset<Row> esData = table.filter("`_error_key` != ''");
            ElasticUtil.INSTANCE.writeDivideDetailToEs(MessageFormat.format(BaseConstant.ES_INDEX_NAME, dataTask.getAppName().toLowerCase(), dataTask.getId()),dataTask.getAppName().toLowerCase(), esData);

            //从redis更新最新的数据集配置。
            dataTask = getOne(dataTask.getId());
            //校验任务
            ValidatorResult validatorResult = ValidatorUtil.validate(dataTask);
            if (validatorResult.isHasErrors()){
                logger.error("重新获取任务，校验任务参数错误 {0}",validatorResult);
                return;
            }

            //是否有存入其他dataSink kafka的需求
            if ((dataTask.getDataSink() != null) && (dataTask.getDataSink().getType() == 1)){
                try {
                    table.toJSON().write()
                            .option("kafka.bootstrap.servers", dataTask.getDataSink().getClusterIp())
                            .option("topic", dataTask.getDataSink().getTopic())
                            .save();
                }catch (Exception e){
                    System.out.println("error "+e);
                }
            }

            //计算数据集
            dataTask.getDataSetRule().forEach(dataSetRule -> {
                //需要select出来的字段，后续参与计算和做group by。其中指标的字段和时间字段必须要选择出来。
                Set<String> selectField = new HashSet<>(8);
                List<Measure> measures = dataSetRule.getDatasetRule().getMeasures();
                String dateField = dataSetRule.getDatasetRule().getDate();
                //添加时间字段
                selectField.add(dateField);
                //添加其他字段
                measures.forEach(measure -> selectField.add(measure.getMeasureA()));
                StringBuilder selectSql = measureCondition(measures);
                selectSql.append("`").append(dateField).append("` ");

                //过滤
                List<Filter> filters = dataSetRule.getDatasetRule().getFilters();
                //过滤条件关系,与 or 或
                String filterOperator = dataSetRule.getDatasetRule().getFilterOperator();
                StringBuilder filterSql = filterCondition(filters,filterOperator);

                selectSql.append(" from `").append(dataSetRule.getName()).append(dataSetRule.getId()).append("`");
                //组合sql
                if (!"".contentEquals(filterSql)){
                    selectSql.append(" where ").append(filterSql);
                }
                //分组条件
                selectSql.append(" group by `").append(dateField).append("`");

                Dataset<Row> dataSet = table.withColumn(dateField,date_format(col(dateField),"yyyy-MM-dd HH:mm").cast(DataTypes.TimestampType));
                //记录正常切分数据量
                dataSet.createOrReplaceTempView(new StringBuilder().append("`").append(dataSetRule.getName()).append(dataSetRule.getId()).append("`").toString());
                Dataset<Row> res = sparkSession.sql(selectSql.toString());

                //需要从聚合结果中提取的字段：指标别名和时间字段
                List<String> influxField = measures.parallelStream().map(Measure::getMeasureLabel).collect(Collectors.toList());
                influxField.add(dateField);
                //数据集聚合结果输出到influx
                DataLandingServiceImpl.INSTANCE.writeGoodDivideAgg(res,INFLUX_DATA_SET_TABLE_PREFIX + dataSetRule.getId(),influxField,dateField);
            });
        }
    }

    @Override
    public void aggBad(SparkSession sparkSession, JavaRDD<Row> javaRDD, StructType schema, DataTask dataTask) {
        //构造表
        Dataset<Row> table = sparkSession.createDataFrame(javaRDD, schema);
        table.cache();
        /*
        是否有数据
        */
        boolean f = table.count() > 0;
        if (f){
            Dataset<Row> dataSet = table.withColumn(SYS_TIME,date_format(col(SYS_TIME),"yyyy-MM-dd HH:mm").cast(DataTypes.TimestampType));
            dataSet.createOrReplaceTempView("exceptionDataSet");
            Dataset<Row> res = dataSet.sqlContext().sql("select _sysTime, last(_line) as last_line, last(message) as last_msg, count(_line) as total from exceptionDataSet group by _sysTime");
            res.cache();
            //记录异常切分数据量、最新的异常日志、错误原因
            DataLandingServiceImpl.INSTANCE.writeDivideCount(res, INFLUX_DIVIDE_DATA_SET, dataTask.getId(), false);
        }
    }
}
