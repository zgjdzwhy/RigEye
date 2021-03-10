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
@Table(name = "contacts")
@AutoId
public class Contacts extends BaseEntity {

	private static final long serialVersionUID = 4147877240501511164L;
	@Id
	private Integer id;
	private String groupName;
	private String realname;
	private String mail;
	private String phone;
	private Date updateTime;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = (groupName == null ? null : groupName.trim());
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = (realname == null ? null : realname.trim());
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = (mail == null ? null : mail.trim());
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = (phone == null ? null : phone.trim());
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Contacts{" +
				"id=" + id +
				", groupName='" + groupName + '\'' +
				", realname='" + realname + '\'' +
				", mail='" + mail + '\'' +
				", phone='" + phone + '\'' +
				", updateTime=" + updateTime +
				'}';
	}
}
