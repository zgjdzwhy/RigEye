package com.rigai.rigeye.web.bean.vo;


import com.rigai.rigeye.alert.bean.AlertRuleConstruct;
import com.rigai.rigeye.alert.bean.AlertTaskInfo;
import com.rigai.rigeye.common.constant.CommonConstant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/23.
 */

public class AlertTaskVO implements Comparable<AlertTaskVO>{
    private Long taskId;
    private String name;
    private String appName;
    private String rule;
    private Date updateTime;
    private Integer status;
    private Integer monitorStatus;

    public AlertTaskVO() {
    }

    public AlertTaskVO(AlertTaskInfo alertTaskInfo){
        this.taskId=alertTaskInfo.getTask().getId();
        StringBuilder builder=new StringBuilder();
        if(alertTaskInfo.getRules()!=null){
            for (AlertRuleConstruct rules:alertTaskInfo.getRules()){
                builder.append(rules.toString());
                builder.append(CommonConstant.SPLIT_SIGN);
            }
            this.rule=builder.toString().substring(0,builder.length()-1);
        }
        this.updateTime=alertTaskInfo.getTask().getUpdateTime();
        this.status=alertTaskInfo.getTask().getStatus();
        this.monitorStatus=alertTaskInfo.getTask().getMonitorStatus();
        this.name=alertTaskInfo.getTask().getName();
        this.appName=alertTaskInfo.getTask().getAppName();
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "AlertTaskVO{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", appName='" + appName + '\'' +
                ", rule='" + rule + '\'' +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                ", monitorStatus=" + monitorStatus +
                '}';
    }

    public static List<AlertTaskVO> toAlertTaskVO(List<AlertTaskInfo> info){
        ArrayList<AlertTaskVO> array=new ArrayList<>(info.size());
        info.parallelStream().forEach(alertTaskInfo -> array.add(new AlertTaskVO(alertTaskInfo)));
        return array;
    }

    @Override
    public int compareTo(AlertTaskVO vo) {
        if(vo==null||vo.getUpdateTime()==null){
            return -1;
        }
        long targetTime=vo.getUpdateTime().getTime();
        long source=this.getUpdateTime().getTime();
        if(targetTime>source){
            return 1;
        }
        if(targetTime<source){
            return -1;
        }
        return 0;
    }
}
