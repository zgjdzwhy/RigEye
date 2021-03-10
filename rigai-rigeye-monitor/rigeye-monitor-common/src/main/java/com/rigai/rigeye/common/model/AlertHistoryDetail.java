package com.rigai.rigeye.common.model;

import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "alert_history_detail")
@AutoId
public class AlertHistoryDetail extends BaseEntity {


	private static final long serialVersionUID = -2739685936342325537L;
	@Id
	private Integer id;
	private String alertRuleIds;
	private String alertName;
	private String alertContent;
	private String alertType;
	private Date alertTime;
	private Integer triggerRuleInterval;
	private Date createTime;
	private Integer taskId;
	private String appName;
	private Integer alertFaultId;
	private String contacts;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getAlertRuleIds() {
		return alertRuleIds;
	}

	public void setAlertRuleIds(String alertRuleIds) {
		this.alertRuleIds = alertRuleIds;
	}

	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = (alertName == null ? null : alertName.trim());
	}
	public String getAlertContent() {
		return alertContent;
	}
	public void setAlertContent(String alertContent) {
		this.alertContent = (alertContent == null ? null : alertContent.trim());
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = (alertType == null ? null : alertType.trim());
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

	public Integer getAlertFaultId() {
		return alertFaultId;
	}

	public void setAlertFaultId(Integer alertFaultId) {
		this.alertFaultId = alertFaultId;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return "AlertHistoryDetail{" +
				"id=" + id +
				", alertRuleIds='" + alertRuleIds + '\'' +
				", alertName='" + alertName + '\'' +
				", alertContent='" + alertContent + '\'' +
				", alertType='" + alertType + '\'' +
				", alertTime=" + alertTime +
				", triggerRuleInterval=" + triggerRuleInterval +
				", createTime=" + createTime +
				", taskId=" + taskId +
				", appName='" + appName + '\'' +
				", alertFaultId=" + alertFaultId +
				", contacts='" + contacts + '\'' +
				'}';
	}
}
