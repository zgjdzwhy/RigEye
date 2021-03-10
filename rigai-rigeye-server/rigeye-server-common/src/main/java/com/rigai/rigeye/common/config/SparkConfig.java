package com.rigai.rigeye.common.config;/**
 * Created by Administrator on 2018/8/21 0021.
 */

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.stereotype.Component;

/**
 * @author wuhuiyong
 * @create 2018-08-21 14:05
 **/
@Component
public class SparkConfig {

    @ApolloConfig("spark")
    private Config config;
    private String otherConf;
    private String executorExtraPath;
    private String driverExtraPath;
    private String deployMode;
    private String appResource;
    private String className;
    private String coresMax;
    private String executorMemory;
    public String getOtherConf() {
        return config.getProperty("spark.dataproc.otherConf","");
    }

    public String getCoresMax() {
        return config.getProperty("spark.dataproc.coresMax","");
    }
    public String getExecutorMemory() {
        return config.getProperty("spark.dataproc.executorMemory","");
    }

    public String getExecutorExtraPath() {
        return config.getProperty("spark.dataproc.executorExtraPath","");
    }

    public String getDriverExtraPath() {
        return config.getProperty("spark.dataproc.driverExtraPath","");
    }

    public String getDeployMode() {
        return config.getProperty("spark.dataproc.deployMode","cluster");
    }

    public String getAppResource() {
        return config.getProperty("spark.dataproc.appResource","");
    }

    public String getClassName() {
        return config.getProperty("spark.dataproc.className","");
    }

}
