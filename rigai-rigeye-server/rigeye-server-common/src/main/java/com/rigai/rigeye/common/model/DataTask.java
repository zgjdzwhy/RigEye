package com.rigai.rigeye.common.model;

import javax.persistence.Id;
import javax.persistence.Table;

import com.em.fx.core.annotation.LogicDelete;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.em.fx.core.entity.BaseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "data_task")
@AutoId
public class DataTask extends BaseEntity {
	private static final long serialVersionUID = 6618016694161717526L;
	@Id
	private Long id;
	private String taskName;
	private String appName;
	private String appRunId;
	private Integer dataSourceId;
	private String encode;
	private String logSample;
	private String logAnalysisXml;
	private Integer monitorType;
	private Integer taskStatus;
	private Integer monitorStatus;
	private Integer cleanType;
	private String userId;
    @LogicDelete
	private String isDel;
    private String model;
    private Integer dataSinkId;
    private String startParam;
    /*offset位置，默认最新位置*/
    private String offset;
    private Timestamp createTime;
    private Timestamp updateTime;

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getAppRunId() {
		return appRunId;
	}

	public void setAppRunId(String appRunId) {
		this.appRunId = appRunId;
	}

	public DataTask init(){
		this.taskName = "";
		this.appName = "";
		this.dataSourceId = -1;
		this.encode = "";
		this.logSample = "";
		this.logAnalysisXml = "";
		this.monitorType = 1;
		this.taskStatus = 1;
		this.monitorStatus = 1;
		this.cleanType = 1;
		this.userId = "";
		this.createTime = Timestamp.valueOf(LocalDateTime.now());
		this.updateTime = Timestamp.valueOf(LocalDateTime.now());
		this.isDel = "0";
        this.model = "";
        this.dataSinkId = -1;
        this.startParam = "";
        this.appRunId = "";
        this.offset = "latest";
		return this;
	}

    public Integer getDataSinkId() {
        return dataSinkId;
    }

    public void setDataSinkId(Integer dataSinkId) {
        this.dataSinkId = dataSinkId;
    }

    public String getStartParam() {
        return startParam;
    }

    public void setStartParam(String startParam) {
        this.startParam = startParam;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = (taskName == null ? null : taskName.trim());
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = (appName == null ? null : appName.trim());
	}

	public Integer getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getLogSample() {
		return logSample;
	}

	public void setLogSample(String logSample) {
		this.logSample = logSample;
	}

	public String getLogAnalysisXml() {
		return logAnalysisXml;
	}

	public void setLogAnalysisXml(String logAnalysisXml) {
		this.logAnalysisXml = logAnalysisXml;
	}

	public Integer getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(Integer monitorType) {
		this.monitorType = monitorType;
	}

	public Integer getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(Integer taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Integer getMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(Integer monitorStatus) {
		this.monitorStatus = monitorStatus;
	}

	public Integer getCleanType() {
		return cleanType;
	}

	public void setCleanType(Integer cleanType) {
		this.cleanType = cleanType;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = (userId == null ? null : userId.trim());
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

    @Override
	public String toString() {
		return "DataTask{" +
				"id=" + id +
				", taskName='" + taskName + '\'' +
				", appName='" + appName + '\'' +
				", dataSourceId=" + dataSourceId +
				", encode='" + encode + '\'' +
				", logSample='" + logSample + '\'' +
				", logAnalysisXml='" + logAnalysisXml + '\'' +
				", monitorType='" + monitorType + '\'' +
				", taskStatus='" + taskStatus + '\'' +
				", monitorStatus='" + monitorStatus + '\'' +
				", cleanType='" + cleanType + '\'' +
				", userId='" + userId + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", isDel='" + isDel + '\'' +
				'}';
	}
}
