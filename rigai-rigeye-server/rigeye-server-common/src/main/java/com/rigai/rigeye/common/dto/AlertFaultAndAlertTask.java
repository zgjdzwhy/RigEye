package com.rigai.rigeye.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author yh
 * @date 2018/9/6 18:26
 */
public class AlertFaultAndAlertTask {
    private Integer id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date alertDate;
    private String triggerRuleDesc;
    private Integer alertNum;
    private String appName;
    private String level;
    private String monitorMarket;
    private Long taskId;

    public String getMonitorMarket() {
        return monitorMarket;
    }

    public void setMonitorMarket(String monitorMarket) {
        this.monitorMarket = monitorMarket;
    }

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

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getTriggerRuleDesc() {
        return triggerRuleDesc;
    }

    public void setTriggerRuleDesc(String triggerRuleDesc) {
        this.triggerRuleDesc = triggerRuleDesc;
    }

    public Integer getAlertNum() {
        return alertNum;
    }

    public void setAlertNum(Integer alertNum) {
        this.alertNum = alertNum;
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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "AlertFaultAndAlertTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alertDate=" + alertDate +
                ", triggerRuleDesc='" + triggerRuleDesc + '\'' +
                ", alertNum=" + alertNum +
                ", appName='" + appName + '\'' +
                ", level='" + level + '\'' +
                ", monitorMarket='" + monitorMarket + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
