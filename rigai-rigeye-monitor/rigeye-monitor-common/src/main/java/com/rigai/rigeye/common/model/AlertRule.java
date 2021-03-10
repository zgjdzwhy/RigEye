package com.rigai.rigeye.common.model;

import com.em.fx.core.annotation.LogicDelete;
import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "alert_rule")
@AutoId
public class AlertRule extends BaseEntity {

	private static final long serialVersionUID = -1524943710442818368L;
	@Id
	private Long id;
	private Long alertTaskId;
	private String channel;
	private Long period;
	private String variable;
	private Integer valueType;
	private Integer comparisonOperators;
	private Long threshold;
	private Date createTime;
	private Date updateTime;
	private String userId;
	@LogicDelete
	private Integer isDel;
	private String norm;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAlertTaskId() {
		return alertTaskId;
	}
	public void setAlertTaskId(Long alertTaskId) {
		this.alertTaskId = alertTaskId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Integer getComparisonOperators() {
		return comparisonOperators;
	}

	public void setComparisonOperators(Integer comparisonOperators) {
		this.comparisonOperators = comparisonOperators;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	public String getNorm() {
		return norm;
	}

	public void setNorm(String norm) {
		this.norm = norm;
	}


	@Override
	public String toString() {
		return "AlertRule{" +
				"id=" + id +
				", alertTaskId=" + alertTaskId +
				", channel='" + channel + '\'' +
				", period=" + period +
				", variable='" + variable + '\'' +
				", valueType=" + valueType +
				", comparisonOperators=" + comparisonOperators +
				", threshold=" + threshold +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", userId='" + userId + '\'' +
				", isDel=" + isDel +
				", norm='" + norm + '\'' +
				'}';
	}
}
