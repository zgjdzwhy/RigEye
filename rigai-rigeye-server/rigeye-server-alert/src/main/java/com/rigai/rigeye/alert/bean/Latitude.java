package com.rigai.rigeye.alert.bean;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/16.
 */

public class Latitude {

    /**
     * 纬度名
     */
    @NotNull
    private String key;
    /**
     * 纬度操作符
     */
    @NotNull
    @Min(0)
    @Max(4)
    private Integer operator;
    /**
     * 纬度值
     */
    @NotNull
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Latitude{" +
                "key='" + key + '\'' +
                ", operator='" + operator + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
