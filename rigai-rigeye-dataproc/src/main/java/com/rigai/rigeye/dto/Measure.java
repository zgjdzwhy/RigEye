package com.rigai.rigeye.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/10 15:50
 */
public class Measure implements Serializable {
    private String measureLabel;
    private String measureA;
    private List<String> aggregates;

    public String getMeasureLabel() {
        return measureLabel;
    }

    public void setMeasureLabel(String measureLabel) {
        this.measureLabel = measureLabel;
    }

    public String getMeasureA() {
        return measureA;
    }

    public void setMeasureA(String measureA) {
        this.measureA = measureA;
    }

    public List<String> getAggregates() {
        return aggregates;
    }

    public void setAggregates(List<String> aggregates) {
        this.aggregates = aggregates;
    }
}
