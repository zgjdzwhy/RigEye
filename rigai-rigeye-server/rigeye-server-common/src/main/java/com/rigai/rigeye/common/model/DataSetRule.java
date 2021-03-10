package com.rigai.rigeye.common.model;

import com.rigai.rigeye.common.dto.Rule;
import com.em.fx.core.annotation.LogicDelete;
import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "dataset_rule")
@AutoId
public class DataSetRule extends BaseEntity {

	private static final long serialVersionUID = 6618016694161717526L;
	@Id
	private Long id;
    @NotNull
	private String name;
    @Transient
    private String taskName;
    @Min(1)
	private Long taskId;
    @NotNull
	private Integer status;
    @NotNull
	private Integer type;
    @NotNull(message = "数据集不能为空")
	private Rule datasetRule;
	@LogicDelete
    @JsonIgnore
	private String isDel;
	@JsonIgnore
    private String userId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime ;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Rule getDatasetRule() {
        return datasetRule;
    }

    public void setDatasetRule(Rule datasetRule) {
        this.datasetRule = datasetRule;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

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
		this.name = name;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

    @Override
    public String toString() {
        return "DataSetRule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                ", type=" + type +
                ", datasetRule=" + datasetRule +
                ", isDel='" + isDel + '\'' +
                ", userId='" + userId + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
