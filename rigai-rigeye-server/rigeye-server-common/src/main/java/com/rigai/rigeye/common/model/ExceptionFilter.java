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
@Table(name = "exception_filter")
@AutoId
public class ExceptionFilter extends BaseEntity {


	private static final long serialVersionUID = 1083043517101334451L;
	@Id
	private Integer id;
	private String appName;
	private Integer filterContentId;
	@LogicDelete
	private Integer idDel;
	private String userId;

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
		this.appName = (appName == null ? null : appName.trim());
	}

	public Integer getFilterContentId() {
		return filterContentId;
	}

	public void setFilterContentId(Integer filterContentId) {
		this.filterContentId = filterContentId;
	}

	public Integer getIdDel() {
		return idDel;
	}
	public void setIdDel(Integer idDel) {
		this.idDel = idDel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}

	@Override
	public String toString() {
		return "ExceptionFilter{" +
				"id=" + id +
				", appName='" + appName + '\'' +
				", filterContentId='" + filterContentId + '\'' +
				", idDel=" + idDel +
				", userId='" + userId + '\'' +
				'}';
	}
}
