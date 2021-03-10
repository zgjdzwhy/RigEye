package com.rigai.rigeye.common.model;

import javax.persistence.Id;
import javax.persistence.Table;
import com.em.fx.core.mybatis.annotation.AutoId;
import com.em.fx.core.entity.BaseEntity;
import java.util.Date;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Table(name = "DATA_PROCESSOR_ERROR")
@AutoId
public class DataProcessorError extends BaseEntity {


	private static final long serialVersionUID = 7372908235344938026L;
	private Integer id;
	private Date startTime;
	private Date endTime;
	private Integer num;
	private String errorType;
	private String errorDesc;
	private String errorSample;
	private Date createTime;
	private Integer type;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = (errorType == null ? null : errorType.trim());
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = (errorDesc == null ? null : errorDesc.trim());
	}
	public String getErrorSample() {
		return errorSample;
	}
	public void setErrorSample(String errorSample) {
		this.errorSample = (errorSample == null ? null : errorSample.trim());
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DataProcessorError{" +
				"id=" + id +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", num=" + num +
				", errorType='" + errorType + '\'' +
				", errorDesc='" + errorDesc + '\'' +
				", errorSample='" + errorSample + '\'' +
				", createTime=" + createTime +
				", type=" + type +
				'}';
	}
}
