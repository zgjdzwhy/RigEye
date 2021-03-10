package com.rigai.rigeye.common.bean;

import com.rigai.rigeye.common.model.ExceptionContent;
import com.rigai.rigeye.common.model.ExceptionFilter;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/30.
 */

public class ExceptionFilterListDTO {


    private Integer id;
    private String appName;
    private Integer filterContentId;
    private String abnormalContent;
    private Integer type;
    private String name;

    public ExceptionFilterListDTO() {
    }

    public ExceptionFilterListDTO(ExceptionContent content, ExceptionFilter filter) {
        this.id=filter.getId();
        this.appName=filter.getAppName();
        this.filterContentId=content.getId();
        this.abnormalContent=content.getAbnormalContent();
        this.type=content.getType();
        this.name=content.getName();
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

    public Integer getFilterContentId() {
        return filterContentId;
    }

    public void setFilterContentId(Integer filterContentId) {
        this.filterContentId = filterContentId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExceptionFilterVO{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", filterContentId=" + filterContentId +
                ", abnormalContent='" + abnormalContent + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }


}
