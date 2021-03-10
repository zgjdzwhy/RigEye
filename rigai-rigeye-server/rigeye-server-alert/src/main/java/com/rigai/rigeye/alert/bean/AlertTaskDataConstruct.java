package com.rigai.rigeye.alert.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rigai.rigeye.common.model.AlertTask;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/17.
 * 报警任务的实际数据结构
 */


public class AlertTaskDataConstruct {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String appName;
    @NotNull
    @Size(min = 1)
    @Valid
    private List<AlertDataSetConstruct> alertDataSet;
    @NotNull
    private Integer muchRuleCondition;
    @Min(0)
    @Max(1)
    private Integer monitorStatus;
    @NotNull
    private String level;
    private Integer status;
    @NotNull
    private String noticeTitle;
    @NotNull
    private String noticeTemplate;
    @NotNull
    private String noticeStartTime;
    @NotNull
    private String noticeEndTime;
    @NotNull
    private Integer type;
    private Date createTime;

    private Date updateTime;

    public AlertTaskDataConstruct() {
    }

    public AlertTaskDataConstruct(AlertTask task) {
        //通常拷贝
        this.id=task.getId();
        this.appName=task.getAppName();
        this.createTime=task.getCreateTime();
        this.level=task.getLevel();
        this.monitorStatus=task.getMonitorStatus();
        this.name=task.getName();
        this.noticeEndTime=task.getNoticeEndTime();
        this.noticeStartTime=task.getNoticeStartTime();
        this.noticeTemplate=task.getNoticeTemplate();
        this.muchRuleCondition=task.getMuchRuleCondition();
        this.type=task.getType();
        this.noticeTitle=task.getNoticeTitle();
        this.createTime=task.getCreateTime();
        this.updateTime=task.getUpdateTime();
        this.status=task.getStatus();
        //数据集数组
        JSONArray alertDataSetJsonArray=JSONObject.parseArray(task.getAlertDataSet());
        ArrayList<AlertDataSetConstruct> alertDataSetConstructs=new ArrayList<>(alertDataSetJsonArray.size());
        //循环外层
        for(int i=0;i<alertDataSetJsonArray.size();i++){
            JSONObject alertDataSetJson=alertDataSetJsonArray.getJSONObject(i);
            AlertDataSetConstruct alertDataSet=new AlertDataSetConstruct();
            //变量
            alertDataSet.setVariable(alertDataSetJson.getString("variable"));
            //数据集
            alertDataSet.setDataSet(alertDataSetJson.getString("dataSet"));
            //纬度数组
            JSONArray latitudeJson=alertDataSetJson.getJSONArray("latitudes");
            List<Latitude> latitudes=JSONObject.parseArray(latitudeJson.toJSONString(),Latitude.class);
            alertDataSet.setLatitudes(latitudes);
            alertDataSetConstructs.add(alertDataSet);
        }
        this.alertDataSet=alertDataSetConstructs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getMuchRuleCondition() {
        return muchRuleCondition;
    }

    public void setMuchRuleCondition(Integer muchRuleCondition) {
        this.muchRuleCondition = muchRuleCondition;
    }

    public Integer getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(Integer monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTemplate() {
        return noticeTemplate;
    }

    public void setNoticeTemplate(String noticeTemplate) {
        this.noticeTemplate = noticeTemplate;
    }

    public String getNoticeStartTime() {
        return noticeStartTime;
    }

    public void setNoticeStartTime(String noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    public String getNoticeEndTime() {
        return noticeEndTime;
    }

    public void setNoticeEndTime(String noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<AlertDataSetConstruct> getAlertDataSet() {
        return alertDataSet;
    }

    public void setAlertDataSet(List<AlertDataSetConstruct> alertDataSet) {
        this.alertDataSet = alertDataSet;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AlertTaskDataConstruct{" +
                "type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", appName='" + appName + '\'' +
                ", alertDataSet=" + alertDataSet +
                ", muchRuleCondition=" + muchRuleCondition +
                ", monitorStatus=" + monitorStatus +
                ", level='" + level + '\'' +
                ", status='" + status + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeTemplate='" + noticeTemplate + '\'' +
                ", noticeStartTime='" + noticeStartTime + '\'' +
                ", noticeEndTime='" + noticeEndTime + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public AlertTask toAlertTask(){
        AlertTask alertTask=new AlertTask();
        //直接获取的值
        alertTask.setId(this.getId());
        alertTask.setAppName(this.getAppName());
        alertTask.setCreateTime(this.getCreateTime());
        alertTask.setLevel(this.getLevel());
        alertTask.setMonitorStatus(this.getMonitorStatus());
        alertTask.setName(this.getName());
        alertTask.setNoticeEndTime(this.getNoticeEndTime());
        alertTask.setNoticeStartTime(this.getNoticeStartTime());
        alertTask.setNoticeTemplate(this.getNoticeTemplate());
        alertTask.setMuchRuleCondition(this.getMuchRuleCondition());
        alertTask.setType(this.getType());
        alertTask.setNoticeTitle(this.getNoticeTitle());
        alertTask.setStatus(this.getStatus());
        //特殊赋值
        alertTask.setIsDel("0");
        alertTask.setUpdateTime(Calendar.getInstance().getTime());
        alertTask.setAlertDataSet(JSONObject.toJSONString(this.getAlertDataSet()));
        return alertTask;
    }

}
