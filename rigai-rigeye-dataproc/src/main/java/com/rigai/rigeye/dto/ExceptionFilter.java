package com.rigai.rigeye.dto;

import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/29 15:12
 */
public class ExceptionFilter implements Serializable {
    private Integer id;
    private String appName;
    private Integer filterContentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getFilterContentId() {
        return filterContentId;
    }

    public void setFilterContentId(Integer filterContentId) {
        this.filterContentId = filterContentId;
    }
}
