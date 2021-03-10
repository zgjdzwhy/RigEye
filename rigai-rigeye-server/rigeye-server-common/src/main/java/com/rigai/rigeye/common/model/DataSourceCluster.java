package com.rigai.rigeye.common.model;

import com.em.fx.core.entity.BaseEntity;
import com.em.fx.core.mybatis.annotation.AutoId;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/10/9.
 */

@Table(name = "data_source_cluster")
@AutoId
public class DataSourceCluster extends BaseEntity{
    private static final long serialVersionUID = 2955636034129033445L;

    @Id
    private Long clusterId;
    private String clusterName;
    private String clusterIp;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    @Override
    public String toString() {
        return "DataSourceCluster{" +
                "clusterId=" + clusterId +
                ", clusterName='" + clusterName + '\'' +
                ", clusterIp='" + clusterIp + '\'' +
                '}';
    }
}
