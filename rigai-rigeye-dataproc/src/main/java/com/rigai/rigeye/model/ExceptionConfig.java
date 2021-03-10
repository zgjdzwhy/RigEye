package com.rigai.rigeye.model;

import com.rigai.rigeye.dto.ExceptionContent;
import com.rigai.rigeye.dto.ExceptionFilter;

import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/29 15:26
 */
public class ExceptionConfig implements Serializable {
    private List<ExceptionContent> contents;
    private List<ExceptionFilter> filters;

    public List<ExceptionContent> getContents() {
        return contents;
    }

    public void setContents(List<ExceptionContent> contents) {
        this.contents = contents;
    }

    public List<ExceptionFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ExceptionFilter> filters) {
        this.filters = filters;
    }
}
