package com.rigai.rigeye.common.model;

import javax.persistence.Id;
import javax.persistence.Table;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.em.fx.core.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "DEVIDE_MODEL")
@AutoId
public class DevideModel extends BaseEntity {
	private static final long serialVersionUID = -3525335924305265142L;
	@Id
	private Long id;
	private Long taskId;
	private String model;
	private Timestamp updateTime;
	private Timestamp createTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
