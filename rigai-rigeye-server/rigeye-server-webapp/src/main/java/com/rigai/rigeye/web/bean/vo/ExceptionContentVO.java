package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/27.
 */

public class ExceptionContentVO {
    private Integer id;
    @NotNull
    private String appName;
    @NotNull
    private String abnormalContent;
    @NotNull
    private String name;
    @NotNull
    @Min(0)
    @Max(1)
    private Integer type;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExceptionContentVO{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", abnormalContent='" + abnormalContent + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
