package com.rigai.rigeye.common.dto;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/10 15:57
 */
public class Rule {
    private String date;
    private String filterOperator;
    private List<Dimension> requiredDims;
    private List<Dimension> optionalDims;
    private List<Dimension> dimensions;
    private List<Dimension> sampleDims;
    private List<Filter> filters;
    private List<Measure> measures;
    private List<DerivedMeasure> derivedMeasures;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilterOperator() {
        return filterOperator;
    }

    public void setFilterOperator(String filterOperator) {
        this.filterOperator = filterOperator;
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

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    public List<Dimension> getSampleDims() {
        return sampleDims;
    }

    public void setSampleDims(List<Dimension> sampleDims) {
        this.sampleDims = sampleDims;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public List<DerivedMeasure> getDerivedMeasures() {
        return derivedMeasures;
    }

    public void setDerivedMeasures(List<DerivedMeasure> derivedMeasures) {
        this.derivedMeasures = derivedMeasures;
    }
}
