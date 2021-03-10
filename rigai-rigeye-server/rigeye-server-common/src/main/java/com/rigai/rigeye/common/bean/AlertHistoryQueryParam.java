package com.rigai.rigeye.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/5.
 */


public class AlertHistoryQueryParam {
    private Integer taskId;
    private String alertName;
    private String alertType;
    private String appName;
    private Integer alertFaultId;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date beginTime;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public Integer getAlertFaultId() {
        return alertFaultId;
    }

    public void setAlertFaultId(Integer alertFaultId) {
        this.alertFaultId = alertFaultId;
    }

    @Override
    public String toString() {
        return "AlertHistoryQueryParam{" +
                "taskId=" + taskId +
                ", alertName='" + alertName + '\'' +
                ", alertType='" + alertType + '\'' +
                ", appName='" + appName + '\'' +
                ", alertFaultId=" + alertFaultId +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }
}
