package com.rigai.rigeye.data.service;/**
 * Created by Administrator on 2018/8/21 0021.
 */

import com.em.fx.common.bean.Result;

import java.util.Map;

/**
 * @author wuhuiyong
 * @create 2018-08-21 10:29
 **/
public interface SparkEngine {

    Result submitAppTask(Map<String,String> sparkConfMap,String[] args);

    Result getAppTaskStatus(String appId);

    Result killAppTask(String appId);
}
