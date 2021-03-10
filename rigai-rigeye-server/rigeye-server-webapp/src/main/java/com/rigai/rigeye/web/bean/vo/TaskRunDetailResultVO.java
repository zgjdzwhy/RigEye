package com.rigai.rigeye.web.bean.vo;

import java.util.List;
import java.util.Map;

/**
 * @author yh
 * @date 2018/9/4 16:45
 */
public class TaskRunDetailResultVO {
    private String dateKey;
    private Map<String, Object> dimData;
    private List<String> dimensions;
    private List<String> dims;
    private int intervalInSec;
    private List<String> measures;

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }

    public Map<String, Object> getDimData() {
        return dimData;
    }

    public void setDimData(Map<String, Object> dimData) {
        this.dimData = dimData;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getDims() {
        return dims;
    }

    public void setDims(List<String> dims) {
        this.dims = dims;
    }

    public int getIntervalInSec() {
        return intervalInSec;
    }

    public void setIntervalInSec(int intervalInSec) {
        this.intervalInSec = intervalInSec;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public void setMeasures(List<String> measures) {
        this.measures = measures;
    }
}
