package com.rigai.rigeye.common.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.em.fx.core.annotation.LogicDelete;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.em.fx.core.entity.BaseEntity;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "exception_content")
@AutoId
public class ExceptionContent extends BaseEntity {


	private static final long serialVersionUID = 9198421100429130835L;
	@Id
	private Integer id;
	private String appName;
	private String abnormalContent;
	private Integer type;
	@LogicDelete
	private Integer isDel;
	private String userId;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAbnormalContent() {
		return abnormalContent;
	}

	public void setAbnormalContent(String abnormalContent) {
		this.abnormalContent = abnormalContent;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ExceptionContent{" +
				"id=" + id +
				", appName='" + appName + '\'' +
				", abnormalContent='" + abnormalContent + '\'' +
				", type=" + type +
				", isDel=" + isDel +
				", userId='" + userId + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
