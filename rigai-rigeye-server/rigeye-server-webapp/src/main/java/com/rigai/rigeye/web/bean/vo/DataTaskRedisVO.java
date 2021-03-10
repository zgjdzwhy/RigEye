package com.rigai.rigeye.web.bean.vo;

import com.alibaba.fastjson.JSONArray;
import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.model.DataSetRule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/21 15:13
 */
public class DataTaskRedisVO implements Serializable {
    private static final long serialVersionUID = 5162943578906737586L;
    private Long id;
    private String taskName;
    private String appName;
    /**
     * 日志源
     */
    private DataSourceDTO dataSourceDTO;
    private String encode;
    private String logAnalysisXml;
    private Integer monitorType;
    private Integer cleanType;
    private String offset;
    private JSONArray model;

    /**
     * 明细数据源
     */
    private DataSourceDTO dataSink;
    /**
     * 数据集
     */
    private List<DataSetRule> dataSetRule;
    private String startParam;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public DataSourceDTO getDataSourceDTO() {
        return dataSourceDTO;
    }

    public void setDataSourceDTO(DataSourceDTO dataSourceDTO) {
        this.dataSourceDTO = dataSourceDTO;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public String getLogAnalysisXml() {
        return logAnalysisXml;
    }

    public void setLogAnalysisXml(String logAnalysisXml) {
        this.logAnalysisXml = logAnalysisXml;
    }

    public Integer getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public JSONArray getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = JSONArray.parseArray(model);
    }

    public DataSourceDTO getDataSink() {
        return dataSink;
    }

    public void setDataSink(DataSourceDTO dataSink) {
        this.dataSink = dataSink;
    }

    public List<DataSetRule> getDataSetRule() {
        return dataSetRule;
    }

    public void setDataSetRule(List<DataSetRule> dataSetRule) {
        this.dataSetRule = dataSetRule;
    }

    public String getStartParam() {
        return startParam;
    }

    public void setStartParam(String startParam) {
        this.startParam = startParam;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DataTaskRedisVO{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", appName='" + appName + '\'' +
                ", dataSourceDTO=" + dataSourceDTO +
                ", encode='" + encode + '\'' +
                ", logAnalysisXml='" + logAnalysisXml + '\'' +
                ", monitorType=" + monitorType +
                ", cleanType=" + cleanType +
                ", offset='" + offset + '\'' +
                ", model=" + model +
                ", dataSink=" + dataSink +
                ", dataSetRule=" + dataSetRule +
                ", startParam='" + startParam + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
