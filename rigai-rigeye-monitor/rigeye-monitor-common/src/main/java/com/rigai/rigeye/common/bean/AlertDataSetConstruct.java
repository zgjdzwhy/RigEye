package com.rigai.rigeye.common.bean;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 报警数据集存储结构
 * @author chenxing
 * Created by ChenXing on 2018/8/16.
 */

public class AlertDataSetConstruct {

    /**
     * 变量
     */
    @NotNull
    private String variable;
    /**
     * 数据集
     */
    @NotNull
    private String dataSet;
    /**
     * 纬度
     */
    @Valid
    private List<Latitude> latitudes;

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public List<Latitude> getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(List<Latitude> latitudes) {
        this.latitudes = latitudes;
    }

    @Override
    public String toString() {
        return "AlertDataSetConstruct{" +
                "variable='" + variable + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", latitudes=" + latitudes +
                '}';
    }
}
