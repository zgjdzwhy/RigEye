package com.rigai.rigeye.web.bean.vo;

/**
 * @author yh
 * @date 2018/8/23 14:41
 */
public class DataTaskStatusVO {
    private long id;
    private int taskStatus;
    private int monitorStatus;
    private String monitorMessage;

    public DataTaskStatusVO(long id, int taskStatus, int monitorStatus) {
        this.id = id;
        this.taskStatus = taskStatus;
        this.monitorStatus = monitorStatus;
    }

    public String getMonitorMessage() {
        return monitorMessage;
    }

    public void setMonitorMessage(String monitorMessage) {
        this.monitorMessage = monitorMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(int monitorStatus) {
        this.monitorStatus = monitorStatus;
    }
}
