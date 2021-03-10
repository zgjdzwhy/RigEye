package com.rigai.rigeye.web.bean.vo;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/15 9:01
 */
public class DataSetOperationVO {
    private List<String> dates;
    private DimensionsAndSampleDimsVO dimensions;
    private DimensionsAndSampleDimsVO sampleDims;
    private MeasuresAndFiltersVO filters;
    private MeasuresAndFiltersVO measures;

    public DataSetOperationVO() {
    }

    public DataSetOperationVO(List<String> dates, DimensionsAndSampleDimsVO dimensions, DimensionsAndSampleDimsVO sampleDims, MeasuresAndFiltersVO filters, MeasuresAndFiltersVO measures) {
        this.dates = dates;
        this.dimensions = dimensions;
        this.sampleDims = sampleDims;
        this.filters = filters;
        this.measures = measures;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public DimensionsAndSampleDimsVO getDimensions() {
        return dimensions;
    }

    public void setDimensions(DimensionsAndSampleDimsVO dimensions) {
        this.dimensions = dimensions;
    }

    public DimensionsAndSampleDimsVO getSampleDims() {
        return sampleDims;
    }

    public void setSampleDims(DimensionsAndSampleDimsVO sampleDims) {
        this.sampleDims = sampleDims;
    }

    public MeasuresAndFiltersVO getFilters() {
        return filters;
    }

    public void setFilters(MeasuresAndFiltersVO filters) {
        this.filters = filters;
    }

    public MeasuresAndFiltersVO getMeasures() {
        return measures;
    }

    public void setMeasures(MeasuresAndFiltersVO measures) {
        this.measures = measures;
    }
}
