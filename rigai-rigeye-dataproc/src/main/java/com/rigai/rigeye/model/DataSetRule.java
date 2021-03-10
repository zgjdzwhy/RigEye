package com.rigai.rigeye.model;

import com.rigai.rigeye.dto.Rule;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Admin
 * @date 2018/08/01
 */
public class DataSetRule implements Serializable {
	private Long id;
    @NotNull
	private String name;
    @Min(1)
	private Long taskId;
    @NotNull
	private Integer status;
    @NotNull
	private Integer type;
    @NotNull(message = "数据集不能为空")
	private Rule datasetRule;
	private String isDel;
    private String userId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime ;

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
}
