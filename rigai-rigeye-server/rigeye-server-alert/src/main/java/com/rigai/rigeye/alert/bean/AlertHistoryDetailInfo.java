package com.rigai.rigeye.alert.bean;

import java.util.Date;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/4.
 */

public class AlertHistoryDetailInfo implements Comparable<AlertHistoryDetailInfo>{
    private Integer id;
    private String alertRule;
    private String alertName;
    private String alertContent;
    private String alertType;
    private Date alertTime;
    private Integer triggerRuleInterval;
    private Date createTime;
    private Integer taskId;
    private String appName;
    private String contacts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlertRule() {
        return alertRule;
    }

    public void setAlertRule(String alertRule) {
        this.alertRule = alertRule;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertContent() {
        return alertContent;
    }

    public void setAlertContent(String alertContent) {
        this.alertContent = alertContent;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public Integer getTriggerRuleInterval() {
        return triggerRuleInterval;
    }

    public void setTriggerRuleInterval(Integer triggerRuleInterval) {
        this.triggerRuleInterval = triggerRuleInterval;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "AlertHistoryDetailInfo{" +
                "id=" + id +
                ", alertRule='" + alertRule + '\'' +
                ", alertName='" + alertName + '\'' +
                ", alertContent='" + alertContent + '\'' +
                ", alertType='" + alertType + '\'' +
                ", alertTime=" + alertTime +
                ", triggerRuleInterval=" + triggerRuleInterval +
                ", createTime=" + createTime +
                ", taskId=" + taskId +
                ", appName='" + appName + '\'' +
                ", contacts='" + contacts + '\'' +
                '}';
    }



    @Override
    public int compareTo(AlertHistoryDetailInfo target) {
        if(target==null||target.getAlertTime()==null){
            return 1;
        }
        long source=this.getAlertTime().getTime();
        long targetTime=target.getAlertTime().getTime();
        if(targetTime>source){
            return 1;
        }
        if(targetTime<source){
            return -1;
        }
        return 0;
    }
}
