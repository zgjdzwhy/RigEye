package com.rigai.rigeye.common.dto;

import com.em.fx.core.annotation.LogicDelete;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/9.
 */

public class DataSourceDTO {
    private Integer id;
    private Integer type;
    private String topic;
    private String consumerGroup;
    private Long clusterId;
    private String remark;
    private String userId;
    private String isDel;
    private Date createTime;
    private Date updateTime;
    private String clusterName;
    private String clusterIp;

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

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
        this.topic = topic;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
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
        this.remark = remark;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
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

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public String toString() {
        return "DataSourceDTO{" +
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
                ", clusterName='" + clusterName + '\'' +
                ", clusterIp='" + clusterIp + '\'' +
                '}';
    }
}
