package com.rigai.rigeye.service;

import com.rigai.rigeye.model.DataTask;
import com.rigai.rigeye.model.ExceptionConfig;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.Map;

/**
 * @author yh
 * @date 2018/8/20 14:29
 */
public interface DataProcService {
    DataTask getOne(Long id);

    Row parseLog(ExceptionConfig exceptionConfig, String log, DataTask dataTask, List<Map> list);

    Row parseBadLog(List<Map> list,String message);

    StructType createSchema(DataTask dataTask);

    StructType exceptSchema();

    void aggGood(SparkSession sparkSession, JavaRDD<Row> javaRDD, StructType schema, DataTask dataTask);


    void aggBad(SparkSession sparkSession, JavaRDD<Row> javaRDD, StructType schema, DataTask dataTask);
}
