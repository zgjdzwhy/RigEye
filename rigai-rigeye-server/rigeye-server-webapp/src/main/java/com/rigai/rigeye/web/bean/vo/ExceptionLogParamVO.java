package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author yh
 * @date 2018/9/7 15:33
 */
public class ExceptionLogParamVO {
    @NotNull
    private Integer page;
    @NotNull
    private Integer pageSize;
    private String appName;
    @NotNull
    private Long startTime;
    @NotNull
    private Long endTime;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
