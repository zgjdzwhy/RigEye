package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author yh
 * @date 2018/9/11 14:26
 */
public class LogDetailParamVO {
    private String appName;
    private String level;
    private String template;
    private String log;
    @NotNull
    private Long startTime;
    @NotNull
    private Long endTime;
    @NotNull
    private Integer page;
    @NotNull
    private Integer pageSize;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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
}
