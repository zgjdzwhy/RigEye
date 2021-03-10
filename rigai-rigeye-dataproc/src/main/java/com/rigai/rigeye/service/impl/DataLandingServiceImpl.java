package com.rigai.rigeye.service.impl;

import com.rigai.rigeye.common.ApolloConfig;
import com.rigai.rigeye.service.DataLandingService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author yh
 * @date 2018/8/28 11:23
 */
public enum DataLandingServiceImpl implements DataLandingService, Serializable {
    INSTANCE;

    private static final Logger logger= LoggerFactory.getLogger(DataLandingServiceImpl.class);
    @Override
    public void writeGoodDivideAgg(Dataset<Row> dataset, String measurementName, List<String> influxField, String dateField) {
        String url = ApolloConfig.INSTANCE.getInfluxUrl();
        String user = ApolloConfig.INSTANCE.getInfluxUser();
        String pass = ApolloConfig.INSTANCE.getInfluxPass();
        String db = ApolloConfig.INSTANCE.getInfluxDb();
        dataset.foreachPartition(t -> {
            InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
            try {
                influxDB.setDatabase(db);
                //每个分区的数据批处理一次
                BatchPoints batchPoints = BatchPoints.database(db).build();
                t.forEachRemaining(o -> {
                    Point.Builder builder = Point.measurement(measurementName);
                    GenericRowWithSchema row = (GenericRowWithSchema) o;
                    for (StructField field : row.schema().fields()) {
                        //只选择需要筛选的字段，其他字段不需要。
                        if (!influxField.contains(field.name())) {
                            continue;
                        }
                        DataType dataType = field.dataType();
                        if (dataType.equals(DataTypes.StringType)) {
                            builder.addField(field.name(), (String) row.getAs(field.name()));
                        } else if (dataType.equals(DataTypes.LongType)) {
                            builder.addField(field.name(), (Long) row.getAs(field.name()));
                        } else if (dataType.equals(DataTypes.DoubleType)) {
                            builder.addField(field.name(), (Double) row.getAs(field.name()));
                        } else if (dataType.equals(DataTypes.TimestampType)) {
                            if (field.name().equals(dateField)) {
                                builder.time(((Timestamp) row.getAs(dateField)).getTime(), TimeUnit.MILLISECONDS);
                            } else {
                                builder.addField(field.name(), ((Timestamp) row.getAs(dateField)).getTime());
                            }
                        }
                    }
                    batchPoints.point(builder.build());
                });
                influxDB.write(batchPoints);
            } catch (Exception e) {
                System.out.println("error "+e);
                throw new RuntimeException();
            } finally {
                influxDB.close();
            }
        });
    }

    @Override
    public void writeDivideCount(Dataset<Row> dataset, String measurementName, Long id, boolean type) {
        String url = ApolloConfig.INSTANCE.getInfluxUrl();
        String user = ApolloConfig.INSTANCE.getInfluxUser();
        String pass = ApolloConfig.INSTANCE.getInfluxPass();
        String db = ApolloConfig.INSTANCE.getInfluxDb();
        if (type) {
            dataset.foreachPartition(o -> {
                long start = System.currentTimeMillis();
                InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
                logger.info("连接influxdb消耗(秒)："+(System.currentTimeMillis()-start)/1000);
                try {
                    influxDB.setDatabase(db);
                    start = System.currentTimeMillis();
                    o.forEachRemaining(s -> {
                        //记录切分错误的日志数量。需要从聚合结果中查询_sysTime,
                        GenericRowWithSchema row = (GenericRowWithSchema) s;
                        Timestamp timestamp = row.getAs("_sysTime");
                        long total = row.getAs("total");
                        Point.Builder builder = Point.measurement(measurementName).time(timestamp.getTime(), TimeUnit.MILLISECONDS);
                        builder.addField("total", total);
                        builder.tag("taskId", String.valueOf(id));
                        builder.tag("type", "good");
                        influxDB.write(builder.build());
                    });
                    logger.info("切分正确的日志聚合，统计总数写入influxDB耗时(秒)："+(System.currentTimeMillis()-start)/1000);
                } catch (Exception e) {
                    System.out.println("error "+e);
                }finally {
                    influxDB.close();
                }
            });
        } else {
            dataset.foreachPartition(o -> {
                InfluxDB influxDB = InfluxDBFactory.connect(url, user, pass);
                try {
                    influxDB.setDatabase(db);
                    o.forEachRemaining(s -> {
                        //记录切分错误的日志数量。需要从聚合结果中查询_sysTime,
                        GenericRowWithSchema row = (GenericRowWithSchema) s;
                        Timestamp timestamp = row.getAs("_sysTime");
                        String lastLine = row.getAs("last_line");
                        String lastMsg = row.getAs("last_msg");
                        long total = row.getAs("total");
                        Point.Builder builder = Point.measurement(measurementName).time(timestamp.getTime(), TimeUnit.MILLISECONDS);
                        builder.addField("total", total);
                        builder.addField("last_msg", lastMsg);
                        builder.tag("last_line", lastLine);
                        builder.tag("taskId", String.valueOf(id));
                        builder.tag("type", "bad");
                        influxDB.write(builder.build());
                    });
                } catch (Exception e) {
                    System.out.println("error "+e);
                    throw new RuntimeException();
                } finally {
                    influxDB.close();
                }
            });
        }
    }
}
