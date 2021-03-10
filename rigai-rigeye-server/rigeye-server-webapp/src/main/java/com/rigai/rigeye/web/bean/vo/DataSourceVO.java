package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/6.
 */

public class DataSourceVO {
    private Integer id;
    @NotNull
    private Integer type;
    @NotNull
    private String topic;
    @NotNull
    private String consumerGroup;
    @NotNull
    private Long clusterId;
    private String remark;
    private String clusterIp;
    private String clusterName;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public String toString() {
        return "DataSourceVO{" +
                "id=" + id +
                ", type=" + type +
                ", topic='" + topic + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", clusterId='" + clusterId + '\'' +
                ", remark='" + remark + '\'' +
                ", clusterIp='" + clusterIp + '\'' +
                ", clusterName='" + clusterName + '\'' +
                '}';
    }
}
