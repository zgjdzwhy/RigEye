package com.rigai.rigeye.web.bean.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/8 16:43
 */
public class DataTaskDetailVO implements Serializable {
    private DataSourceVO dataSource;
    private List<DataSetCheckVO> dataSets;
    private String encode;
    private String logSample;
    private String logAnalysisXml;
    private Integer taskStatus;
    private Integer cleanType;
    /*明细数据落地数据源*/
    private DataSourceVO destDataSource;
    private String startParam;

    public DataSourceVO getDestDataSource() {
        return destDataSource;
    }

    public void setDestDataSource(DataSourceVO destDataSource) {
        this.destDataSource = destDataSource;
    }

    public String getStartParam() {
        return startParam;
    }

    public void setStartParam(String startParam) {
        this.startParam = startParam;
    }

    public DataSourceVO getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceVO dataSource) {
        this.dataSource = dataSource;
    }

    public List<DataSetCheckVO> getDataSets() {
        return dataSets;
    }

    public void setDataSets(List<DataSetCheckVO> dataSets) {
        this.dataSets = dataSets;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getLogSample() {
        return logSample;
    }

    public void setLogSample(String logSample) {
        this.logSample = logSample;
    }

    public String getLogAnalysisXml() {
        return logAnalysisXml;
    }

    public void setLogAnalysisXml(String logAnalysisXml) {
        this.logAnalysisXml = logAnalysisXml;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }
}
