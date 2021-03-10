package com.rigai.rigeye.task.commit.bean;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/13.
 * 报警缓存用于将部分报警信息缓存到redis
 * 缓存的信息用于恢复告警和防重复发送
 */

public class AlertHistoryCache implements Serializable{
    private static final long serialVersionUID = 5191548071551425687L;
    /**
     * 报警记录,用于记录每条规则的报警情况，key为报警规则ID，value为报警时间
     * 全规则匹配情况下为空
     */
    private Map<Long,Date> alertMap;

    /**
     * 最后一次报警的报警ID，全规则匹配则ID为-1
     */
    private Long lastRuleId;

    /**
     * 最后一次报警时间
     */
    private Date lastAlertDate;

    public AlertHistoryCache() {
    }

    /**
     * 添加报警记录,会更新最后一次报警规则Id，最后一次报警时间，如果为满足一条就发送则回更新alertMap
     * @param ruleId 规则ID，全规则匹配时传-1
     * @param time 报警时间
     */
    public void addRecord(Long ruleId,Date time){
        if(-1!=ruleId){
            if(this.alertMap==null){
                this.alertMap=new HashMap<>(1);
            }
            this.alertMap.put(ruleId,time);
        }
        this.lastAlertDate=time;
        this.lastRuleId=ruleId;
    }

    public void remove(Long ruleId){
        if(this.alertMap!=null&&this.alertMap.size()>0){
            this.alertMap.remove(ruleId);
        }
    }

    public Date getRuleIdDate(Long ruleId){
        if(this.alertMap!=null&&this.alertMap.size()>0){
            return this.alertMap.get(ruleId);
        }
        return null;
    }

    public Long getLastRuleId() {
        return lastRuleId;
    }

    public void setLastRuleId(Long lastRuleId) {
        this.lastRuleId = lastRuleId;
    }

    public Date getLastAlertDate() {
        return lastAlertDate;
    }

    public void setLastAlertDate(Date lastAlertDate) {
        this.lastAlertDate = lastAlertDate;
    }

    public Map<Long, Date> getAlertMap() {
        return alertMap;
    }

    public void setAlertMap(Map<Long, Date> alertMap) {
        this.alertMap = alertMap;
    }

    @Override
    public String toString() {
        return "AlertHistoryCache{" +
                "alertMap=" + alertMap +
                ", lastRuleId=" + lastRuleId +
                ", lastAlertDate=" + lastAlertDate +
                '}';
    }
}
