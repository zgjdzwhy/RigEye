package com.rigai.rigeye.dto;

import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/29 15:12
 */
public class ExceptionContent implements Serializable {
    private Integer id;
    private String name;
    private String abnormalContent;
    private Integer type;
    private String appName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbnormalContent() {
        return abnormalContent;
    }

    public void setAbnormalContent(String abnormalContent) {
        this.abnormalContent = abnormalContent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
