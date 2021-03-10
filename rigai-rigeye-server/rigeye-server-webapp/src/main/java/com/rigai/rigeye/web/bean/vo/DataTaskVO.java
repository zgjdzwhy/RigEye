package com.rigai.rigeye.web.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author yh
 * @date 2018/8/3 16:37
 */
public class DataTaskVO {
    private Long id;
    private String taskName;
    private String appName;
    private Integer dataSet;
    private Integer taskStatus;
    private Integer cleanType;
    private Integer monitorType;
    private Integer monitorStatus;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Integer getMonitorType() {
        return monitorType;
    }

    public void setMonitorType(Integer monitorType) {
        this.monitorType = monitorType;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getDataSet() {
        return dataSet;
    }

    public void setDataSet(Integer dataSet) {
        this.dataSet = dataSet;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getCleanType() {
        return cleanType;
    }

    public void setCleanType(Integer cleanType) {
        this.cleanType = cleanType;
    }
    public long getCreateTime() {
        return createTime.getTime();
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public long getUpdateTime() {
        return updateTime.getTime();
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DataTaskVO{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", appName='" + appName + '\'' +
                ", dataSet=" + dataSet +
                ", taskStatus=" + taskStatus +
                ", cleanType=" + cleanType +
                ", monitorType=" + monitorType +
                ", monitorStatus='" + monitorStatus + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
