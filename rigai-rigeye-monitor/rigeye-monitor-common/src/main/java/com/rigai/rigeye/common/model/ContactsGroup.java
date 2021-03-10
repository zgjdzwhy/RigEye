package com.rigai.rigeye.common.model;

import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "contacts_group")
@AutoId
public class ContactsGroup extends BaseEntity {


	private static final long serialVersionUID = -4264388902681838693L;
	@Id
	private Integer id;
	private String groupName;

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

}
