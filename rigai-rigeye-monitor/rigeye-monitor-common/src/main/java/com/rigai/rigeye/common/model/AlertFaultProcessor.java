package com.rigai.rigeye.common.model;

import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "alert_fault_processor")
@AutoId
public class AlertFaultProcessor extends BaseEntity {

	private static final long serialVersionUID = -4080031946588344625L;
	@Id
	private Integer id;
	private Long alertTaskId;
	private String alertRuleIds;
	private Date alertDate;
	private String triggerRuleDesc;
	private Integer alertNum;
	private String alertFaultDesc;
	private String realname;
	private Integer status;
	private String appName;
	private java.util.Date updateTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Long getAlertTaskId() {
		return alertTaskId;
	}

	public void setAlertTaskId(Long alertTaskId) {
		this.alertTaskId = alertTaskId;
	}

	public String getAlertRuleIds() {
		return alertRuleIds;
	}

	public void setAlertRuleIds(String alertRuleIds) {
		this.alertRuleIds = alertRuleIds;
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
		this.triggerRuleDesc = (triggerRuleDesc == null ? null : triggerRuleDesc.trim());
	}
	public Integer getAlertNum() {
		return alertNum;
	}
	public void setAlertNum(Integer alertNum) {
		this.alertNum = alertNum;
	}

	public String getAlertFaultDesc() {
		return alertFaultDesc;
	}

	public void setAlertFaultDesc(String alertFaultDesc) {
		this.alertFaultDesc = alertFaultDesc;
	}

	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = (realname == null ? null : realname.trim());
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = (appName == null ? null : appName.trim());
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "AlertFaultProcessor{" +
				"id=" + id +
				", alertTaskId=" + alertTaskId +
				", alertRuleIds='" + alertRuleIds + '\'' +
				", alertDate=" + alertDate +
				", triggerRuleDesc='" + triggerRuleDesc + '\'' +
				", alertNum=" + alertNum +
				", alertFaultDesc='" + alertFaultDesc + '\'' +
				", realname='" + realname + '\'' +
				", status=" + status +
				", appName='" + appName + '\'' +
				", updateTime=" + updateTime +
				'}';
	}
}
