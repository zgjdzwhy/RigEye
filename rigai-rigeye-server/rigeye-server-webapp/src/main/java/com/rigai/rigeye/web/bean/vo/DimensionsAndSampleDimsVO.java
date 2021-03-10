package com.rigai.rigeye.web.bean.vo;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/15 9:05
 */
public class DimensionsAndSampleDimsVO {
    private int maxNum;
    private int minNum;
    private String name;
    private List<String> values;

    public DimensionsAndSampleDimsVO() {
    }

    public DimensionsAndSampleDimsVO(int maxNum, int minNum, String name, List<String> values) {
        this.maxNum = maxNum;
        this.minNum = minNum;
        this.name = name;
        this.values = values;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
