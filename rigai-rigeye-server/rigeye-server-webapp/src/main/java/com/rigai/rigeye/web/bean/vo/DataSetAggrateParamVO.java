package com.rigai.rigeye.web.bean.vo;

import com.rigai.rigeye.common.dto.Dimension;

import java.util.List;

/**
 * @author yh
 * @date 2018/10/9 14:09
 */
public class DataSetAggrateParamVO {
    private Long datasetId;
    private List<Dimension> requiredDims;
    private List<Dimension> optionalDims;
    private Long minTime;
    private Long maxTime;
    private Integer intervalInSec;
    private String orderByKey;
    private String limit;

    public Long getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Long datasetId) {
        this.datasetId = datasetId;
    }

    public List<Dimension> getRequiredDims() {
        return requiredDims;
    }

    public void setRequiredDims(List<Dimension> requiredDims) {
        this.requiredDims = requiredDims;
    }

    public List<Dimension> getOptionalDims() {
        return optionalDims;
    }

    public void setOptionalDims(List<Dimension> optionalDims) {
        this.optionalDims = optionalDims;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public Integer getIntervalInSec() {
        return intervalInSec;
    }

    public void setIntervalInSec(Integer intervalInSec) {
        this.intervalInSec = intervalInSec;
    }

    public String getOrderByKey() {
        return orderByKey;
    }

    public void setOrderByKey(String orderByKey) {
        this.orderByKey = orderByKey;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
