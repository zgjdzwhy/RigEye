package com.rigai.rigeye.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/28 13:37
 */
public interface DataLandingService {
    void writeGoodDivideAgg(Dataset<Row> dataset, String measurementName, List<String> selectField, String dateField);
    void writeDivideCount(Dataset<Row> dataset, String measurementName, Long id, boolean type);
}
