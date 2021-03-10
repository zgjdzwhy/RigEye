package com.rigai.rigeye.common.dto;

import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/10 15:49
 */
public class Filter implements Serializable {
    private String field;
    private String expression;
    private String value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
