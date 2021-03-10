package com.rigai.rigeye.common.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.em.fx.core.annotation.LogicDelete;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.em.fx.core.entity.BaseEntity;
import java.util.Date;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "alert_task")
@AutoId
public class AlertTask extends BaseEntity {

	private static final long serialVersionUID = -435557998444407500L;
	@Id
	private Long id;
	private String name;
	private String appName;
	private String alertDataSet;
	private Integer muchRuleCondition;
	private Date updateTime;
	private Integer monitorStatus;
	private String level;
	private Integer status;
	private String noticeTitle;
	private String noticeTemplate;
	private String noticeStartTime;
	private String noticeEndTime;
	private String userId;
	private Integer type;
	@LogicDelete
	private String isDel;
	private Date createTime;

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
		this.name = (name == null ? null : name.trim());
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = (appName == null ? null : appName.trim());
	}
	public String getAlertDataSet() {
		return alertDataSet;
	}
	public void setAlertDataSet(String alertDataSet) {
		this.alertDataSet = (alertDataSet == null ? null : alertDataSet.trim());
	}

	public Integer getMuchRuleCondition() {
		return muchRuleCondition;
	}

	public void setMuchRuleCondition(Integer muchRuleCondition) {
		this.muchRuleCondition = muchRuleCondition;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
		this.level = (level == null ? null : level.trim());
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
		this.noticeTitle = (noticeTitle == null ? null : noticeTitle.trim());
	}
	public String getNoticeTemplate() {
		return noticeTemplate;
	}
	public void setNoticeTemplate(String noticeTemplate) {
		this.noticeTemplate = (noticeTemplate == null ? null : noticeTemplate.trim());
	}
	public String getNoticeStartTime() {
		return noticeStartTime;
	}
	public void setNoticeStartTime(String noticeStartTime) {
		this.noticeStartTime = (noticeStartTime == null ? null : noticeStartTime.trim());
	}
	public String getNoticeEndTime() {
		return noticeEndTime;
	}
	public void setNoticeEndTime(String noticeEndTime) {
		this.noticeEndTime = (noticeEndTime == null ? null : noticeEndTime.trim());
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "AlertTask{" +
				"id=" + id +
				", name='" + name + '\'' +
				", appName='" + appName + '\'' +
				", alertDataSet='" + alertDataSet + '\'' +
				", muchRuleCondition='" + muchRuleCondition + '\'' +
				", updateTime=" + updateTime +
				", monitorStatus='" + monitorStatus + '\'' +
				", level='" + level + '\'' +
				", status='" + status + '\'' +
				", noticeTitle='" + noticeTitle + '\'' +
				", noticeTemplate='" + noticeTemplate + '\'' +
				", noticeStartTime='" + noticeStartTime + '\'' +
				", noticeEndTime='" + noticeEndTime + '\'' +
				", userId='" + userId + '\'' +
				", type=" + type +
				", isDel=" + isDel +
				", createTime=" + createTime +
				'}';
	}
}
