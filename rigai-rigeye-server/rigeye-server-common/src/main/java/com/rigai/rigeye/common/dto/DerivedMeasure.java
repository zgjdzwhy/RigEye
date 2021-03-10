package com.rigai.rigeye.common.dto;

/**
 * @author yh
 * @date 2018/8/10 15:53
 */
public class DerivedMeasure {
    private String measureExpression;
    private String measureLabel;

    public String getMeasureExpression() {
        return measureExpression;
    }

    public void setMeasureExpression(String measureExpression) {
        this.measureExpression = measureExpression;
    }

    public String getMeasureLabel() {
        return measureLabel;
    }

    public void setMeasureLabel(String measureLabel) {
        this.measureLabel = measureLabel;
    }
}
