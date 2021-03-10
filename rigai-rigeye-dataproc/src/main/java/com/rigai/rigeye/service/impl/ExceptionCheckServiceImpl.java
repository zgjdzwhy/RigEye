package com.rigai.rigeye.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.rigai.rigeye.common.BaseConstant;
import com.rigai.rigeye.dto.ExceptionContent;
import com.rigai.rigeye.dto.ExceptionFilter;
import com.rigai.rigeye.model.DataTask;
import com.rigai.rigeye.model.ExceptionConfig;
import com.rigai.rigeye.service.ExceptionCheckService;
import com.rigai.rigeye.util.JedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yh
 * @date 2018/8/29 15:37
 */
public enum  ExceptionCheckServiceImpl implements ExceptionCheckService, Serializable {
    INSTANCE;
    private static final Logger logger= LoggerFactory.getLogger(ExceptionCheckServiceImpl.class);

    public ExceptionConfig getExceptionConfig(){
        ExceptionConfig exceptionConfig = new ExceptionConfig();
        try(Jedis jedis = JedisPoolUtil.INSTANCE.getResource()){
            String content = jedis.get(BaseConstant.EXCEPTION_CONTENT);
            if (StringUtils.isNotEmpty(content)) {
                List<ExceptionContent> contents = JSON.parseObject(content, new TypeReference<List<ExceptionContent>>() {
                });
                exceptionConfig.setContents(contents);
            }

            String filters = jedis.get(BaseConstant.EXCEPTION_FILTER);
            if (StringUtils.isNotEmpty(filters)) {
                List<ExceptionFilter> exceptionFilters = JSON.parseObject(filters, new TypeReference<List<ExceptionFilter>>() {
                });
                exceptionConfig.setFilters(exceptionFilters);
            }
        }catch (Exception e){
            System.out.println("getExceptionConfig error."+e);
        }
        return exceptionConfig;
    }

    public String getErrorKey(ExceptionConfig exceptionConfig,String line, DataTask dataTask){
        List<ExceptionContent> contents  = exceptionConfig.getContents();
        List<ExceptionFilter> filters = exceptionConfig.getFilters();

        if (contents != null){
            /*
             过滤出本任务需要执行的检测模板。
              1、根据项目名称过滤，筛选出项目名称为all，或项目名和本任务项目名相等的检测模板。
              2、根据过滤模板filters，如果项目名称符合且模板id符合就去掉。
             */
            contents = contents.parallelStream().filter(exceptionContent -> {
                boolean f =  exceptionContent.getAppName().equals(dataTask.getAppName()) || "all".equals(exceptionContent.getAppName());
                //模板id是否在filters里，是就过滤不要这个模板。
                boolean m = false;
                if (filters != null) {
                    m = filters.parallelStream().anyMatch(exceptionFilter -> exceptionFilter.getAppName().equals(dataTask.getAppName()) && exceptionContent.getId().equals(exceptionFilter.getFilterContentId()));
                }
                return f && !m;
            }).collect(Collectors.toList());

            for (ExceptionContent exceptionContent : contents){
                if (exceptionContent.getType() == 1){
                    //先正则匹配
                    if (Pattern.matches(exceptionContent.getAbnormalContent(),line)){
                        return exceptionContent.getName();
                    }
                }
            }
            for (ExceptionContent exceptionContent : contents){
                if (exceptionContent.getType() == 0){
                    //在关键字匹配
                    if (line.contains(exceptionContent.getAbnormalContent())){
                        return exceptionContent.getName();
                    }
                }
            }
            //最后用error 和exception匹配
            String lineLower = line.toLowerCase();
            if (lineLower.contains(BaseConstant.ERROR_STR)){
                return BaseConstant.ERROR_STR;
            }else if (lineLower.contains(BaseConstant.EXCEPTION_STR)){
                return BaseConstant.EXCEPTION_STR;
            }
        }
        return BaseConstant.BLANK;
    }
}
