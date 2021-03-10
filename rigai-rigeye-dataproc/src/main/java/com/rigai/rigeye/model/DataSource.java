package com.rigai.rigeye.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据源
 */
public class DataSource implements Serializable {
    private Integer id;
    @NotNull
    private Integer type;
    @NotNull
    private String topic;
    @NotNull
    private String consumerGroup;
    @NotNull
    private String clusterIp;

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

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }
}
