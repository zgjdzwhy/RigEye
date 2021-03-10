package com.rigai.rigeye.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yh
 * @date 2018/8/20 14:07
 */
public class DataTask implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String taskName;
    @NotNull
    private String appName;
    /**
     * 日志源
     */
    @NotNull
    @Valid
    private DataSource dataSourceDTO;
    @NotNull
    private String encode;
    @NotNull
    private String logAnalysisXml;
    @NotNull
    private Integer monitorType;
    @NotNull
    @Valid
    private List<DivideModel> model;

    /**
     * 明细数据源
     */
    private DataSource dataSink;
    /**
     * 数据集
     */
    @NotNull
    @Valid
    private List<DataSetRule> dataSetRule;
    private String startParam;
    @NotNull
    private String offset;

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    /**
     * 根据切分模型字段名称查询切分字段和类型
     * @param name
     * @return
     */
    public DivideModel findDivideModelByName(String name){
        List<DivideModel> divideModels = model.parallelStream().filter(model1 -> model1.getName().equals(name)).collect(Collectors.toList());
        if (divideModels != null){
            return divideModels.get(0);
        }
        return null;
    }

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

    public DataSource getDataSourceDTO() {
        return dataSourceDTO;
    }

    public void setDataSourceDTO(DataSource dataSourceDTO) {
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

    public List<DivideModel> getModel() {
        return model;
    }

    public void setModel(List<DivideModel> model) {
        this.model = model;
    }

    public DataSource getDataSink() {
        return dataSink;
    }

    public void setDataSink(DataSource dataSink) {
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
}
