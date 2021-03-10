package com.rigai.rigeye.web.bean.vo;

import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/9 11:09
 */
public class DataTaskParamVO implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String encode;
    @NotNull
    private Integer dataSourceId;
    private String logSample;
    private String logAnalysisXml;
    private Integer cleanType;
    private Integer dataSinkId;
    private String startParam;
    @Valid
    private List<DataSetClearModelVO> devideModel;
    @Range(min = 1,max = 3)
    private Integer step;

    public Integer getDataSinkId() {
        return dataSinkId;
    }

    public void setDataSinkId(Integer dataSinkId) {
        this.dataSinkId = dataSinkId;
    }

    public String getStartParam() {
        return startParam;
    }

    public void setStartParam(String startParam) {
        this.startParam = startParam;
    }

    public List<DataSetClearModelVO> getDevideModel() {
        return devideModel;
    }

    public void setDevideModel(List<DataSetClearModelVO> devideModel) {
        this.devideModel = devideModel;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
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
}
