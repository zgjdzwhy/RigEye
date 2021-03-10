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
@Table(name = "data_source")
@AutoId
public class DataSource extends BaseEntity {

	private static final long serialVersionUID = -7866235472683145365L;
	@Id
	private Integer id;
	private Integer type;
	private String topic;
	private String consumerGroup;
	private Long clusterId;
	private String remark;
	private String userId;
	@LogicDelete
	private String isDel;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = (topic == null ? null : topic.trim());
	}
	public String getConsumerGroup() {
		return consumerGroup;
	}
	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = (consumerGroup == null ? null : consumerGroup.trim());
	}

	public Long getClusterId() {
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = (remark == null ? null : remark.trim());
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = (isDel == null ? null : isDel.trim());
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

	@Override
	public String toString() {
		return "DataSource{" +
				"id=" + id +
				", type=" + type +
				", topic='" + topic + '\'' +
				", consumerGroup='" + consumerGroup + '\'' +
				", clusterId=" + clusterId +
				", remark='" + remark + '\'' +
				", userId='" + userId + '\'' +
				", isDel='" + isDel + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
