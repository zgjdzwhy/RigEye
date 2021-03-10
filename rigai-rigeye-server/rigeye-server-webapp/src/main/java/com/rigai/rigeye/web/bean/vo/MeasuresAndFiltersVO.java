package com.rigai.rigeye.web.bean.vo;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/15 9:08
 */
public class MeasuresAndFiltersVO {
    private List<MeasureAndFilterColumnVO> columns;
    private String name;
    private int maxNum;
    private int minNum;

    public MeasuresAndFiltersVO() {
    }

    public MeasuresAndFiltersVO(List<MeasureAndFilterColumnVO> columns, String name, int maxNum, int minNum) {
        this.columns = columns;
        this.name = name;
        this.maxNum = maxNum;
        this.minNum = minNum;
    }

    public List<MeasureAndFilterColumnVO> getColumns() {
        return columns;
    }

    public void setColumns(List<MeasureAndFilterColumnVO> columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
