package com.rigai.rigeye.web.bean.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author yh
 * @date 2018/9/11 14:26
 */
public class LogDetailResultVO {
    private String id;
    private String appName;
    private String level;
    private String log;
    private String template;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    public LogDetailResultVO(String id, String appName, String level,String log, String template, Date time) {
        this.id = id;
        this.appName = appName;
        this.level = level;
        this.log = log;
        this.template = template;
        this.time = time;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
