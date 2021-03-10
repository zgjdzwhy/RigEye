package com.rigai.rigeye.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.rigai.rigeye.common.config.SparkConfig;
import com.rigai.rigeye.data.service.SparkEngine;
import com.em.fx.argus.tracking.As;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.rest.CreateSubmissionResponse;
import org.apache.spark.deploy.rest.KillSubmissionResponse;
import org.apache.spark.deploy.rest.RestSubmissionClient;
import org.apache.spark.deploy.rest.SubmissionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scala.collection.JavaConverters;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 * @date 2018/8/20 0020
 */
@Component
public class SparkEngineImpl implements SparkEngine {
    private Logger logger= LoggerFactory.getLogger(SparkEngineImpl.class);
    private String master;

    private RestSubmissionClient client;
    @Autowired
    SparkConfig sparkConfig;

    public SparkEngineImpl() {
        Config config = ConfigService.getConfig("spark");
        master = config.getProperty("spark.master", "");
        client = new RestSubmissionClient(master);
    }

    /**
     * @return com.em.fx.common.bean.Result
     * @Author wuhuiyong
     * @Description 提交数据处理任务
     * @Date 15:01 2018/8/21 0021
     * @Param [sparkConf]
     **/
    @Override
    public Result submitAppTask(Map<String, String> userConfMap,String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.cores.max", sparkConfig.getCoresMax());//用户默认配置
        sparkConf.set("spark.executor.memory", sparkConfig.getExecutorMemory());//用户默认配置
        sparkConf.setMaster(master);
        sparkConf.set("spark.jars", sparkConfig.getAppResource());
        sparkConf.set("spark.submit.deployMode", sparkConfig.getDeployMode());
        sparkConf.set("spark.driver.supervise", "false");
        sparkConf.set("spark.executor.extraClassPath", sparkConfig.getExecutorExtraPath());
        sparkConf.set("spark.driver.extraClassPath", sparkConfig.getDriverExtraPath());
        //  String json = "{\"spark.jars\":\"streaming-test-1.0-SNAPSHOT.jar\"}";
        //获取扩展配置
        try{
            JSONObject jsonObj = JSON.parseObject(sparkConfig.getOtherConf());
            for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                sparkConf.set(entry.getKey() , entry.getValue().toString());
            }
        }catch (Exception e){
            logger.error("从apollo获取参数有误！",e);
        }

        //用户配置优先使用
        for (String uf : userConfMap.keySet()) {
            sparkConf.set(uf, userConfMap.get(uf));
        }
        String appId;

        Map<String, String> env = filterSystemEnvironment(System.getenv());
        env.put("SPARK_ENV_LOADED", "1");

        try {
            CreateSubmissionResponse response = (CreateSubmissionResponse)
                    RestSubmissionClient.run(sparkConfig.getAppResource(), sparkConfig.getClassName(), args, sparkConf, SparkEngineImpl.toScalaMap(env));
            appId = response.submissionId();
            if ("".equals(appId)) {
                return new Result(RspCode.EXCEPTION, "提交失败，请检查参数是否正确");
            } else {
                return new Result(RspCode.SUCCESS, "成功", appId);
            }
        } catch (Exception e) {
            logger.error("提交任务异常：", e);
            As.logError("提交任务异常：", e);
            return new Result(RspCode.EXCEPTION, "提交任务异常");
        }
    }


    /**
     * @return com.em.fx.common.bean.Result
     * @ taskStatus{WAITING 等待4，RUNNING 任务运行中2,KILLED 任务停止3，FINISHED 任务结束3 ,FAILED 任务启动失败0}
     * @Author wuhuiyong
     * @Description 查询任务状态
     * @Date 11:28 2018/8/21 0021
     * @Param [appId]
     **/
    @Override
    public Result getAppTaskStatus(String appId) {
        try {
            SubmissionStatusResponse response = (SubmissionStatusResponse) client.requestSubmissionStatus(appId, true);
            int taskStatus = statusConvert(response.driverState());
            return new Result(RspCode.SUCCESS, "成功", taskStatus);
        } catch (Exception e) {
            logger.error("查询任务状态异常：",e);
            As.logError("查询任务状态异常：", e);
            return new Result(RspCode.EXCEPTION, "查询任务状态异常");
        }
    }

    /**
     * @return java.lang.String
     * @Author wuhuiyong
     * @Description 停止任务
     * @Date 10:54 2018/8/21 0021
     * @Param [appId]
     **/
    @Override
    public Result killAppTask(String appId) {
        try {
            KillSubmissionResponse response = (KillSubmissionResponse)client.killSubmission(appId);
            return new Result(RspCode.SUCCESS, "任务停止成功！");
        } catch (Exception e) {
            logger.error("任务停止异常:",e);
            As.logError("任务停止异常：", e);
            return new Result(RspCode.EXCEPTION, "任务停止异常");
        }
    }

    public static Map<String, String> filterSystemEnvironment(Map<String, String> env) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> kv : env.entrySet()) {
            if (kv.getKey().startsWith("SPARK_") && kv.getKey() != "SPARK_ENV_LOADED"
                    || kv.getKey().startsWith("MESOS_")) {
                map.put(kv.getKey(), kv.getValue());
            }
        }
        return map;
    }

    /**
     * @return scala.collection.immutable.Map<A,B>
     * @Author wuhuiyong
     * @Description java map 转 scala map
     * @Date 12:29 2018/8/21 0021
     * @Param [m]
     **/
    public static <A, B> scala.collection.immutable.Map<A, B> toScalaMap(Map<A, B> m) {
        scala.collection.mutable.Map<A, B> mapTest = JavaConverters.mapAsScalaMapConverter(m).asScala();
        Object objTest = scala.collection.immutable.Map$.MODULE$.<A, B>newBuilder().$plus$plus$eq(mapTest.toSeq());
        Object resultTest = ((scala.collection.mutable.Builder) objTest).result();
        scala.collection.immutable.Map<A, B> mapEnv = (scala.collection.immutable.Map) resultTest;
        return mapEnv;
    }

    public int statusConvert(String status) {
        if (status == null){
            return 4;
        }
        switch (status) {
            case "WAITING":
                return 2;
            case "RUNNING":
                return 3;
            case "KILLED":
            case "FINISHED":
                return 5;
            case "FAILED":
                return 4;
            case "ERROR":
                return 4;
            default:
                return -1;
        }
    }
}
