package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/28.
 */

public class ExceptionFilterVO {
    private Integer id;
    @NotNull
    private String appName;
    @NotNull
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

    @Override
    public String toString() {
        return "ExceptionFilterVO{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", filterContentId='" + filterContentId + '\'' +
                '}';
    }
}
