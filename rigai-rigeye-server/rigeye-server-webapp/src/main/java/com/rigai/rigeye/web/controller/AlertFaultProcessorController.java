package com.rigai.rigeye.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.bean.AlertProcessSearchParamVO;
import com.rigai.rigeye.common.dto.AlertFaultAndAlertTask;
import com.rigai.rigeye.common.service.AlertFaultProcessorService;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author yh
 * @date 2018/9/7 10:03
 */
@RestController
@RequestMapping("/alert/process")
public class AlertFaultProcessorController {
    private static final Logger logger = LoggerFactory.getLogger(AlertFaultProcessorController.class);

    @Autowired
    AlertFaultProcessorService alertFaultProcessorService;

    @PostMapping("/getProcessList")
    public Result getAlertProcessList(@Valid @RequestBody PageInfoVO<AlertProcessSearchParamVO> pageInfoVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            logger.info("get dataTasks wrong param {} ",pageInfoVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try{
            PageHelper.startPage(pageInfoVO.getPage(),pageInfoVO.getPageSize());
            List<AlertFaultAndAlertTask> list = alertFaultProcessorService.getAlertFaultAgg(pageInfoVO.getInfo());
            //设置监控大盘
            Config config = ConfigService.getConfig("api");
            String monitorMarket = config.getProperty("monitor.market","");
            if (StringUtils.isNotEmpty(monitorMarket)){
                JSONObject jsonObject = JSON.parseObject(monitorMarket);
                Set<String> keys = jsonObject.keySet();
                list.parallelStream().forEachOrdered(alertFaultAndAlertTask -> {
                    if (keys.contains(alertFaultAndAlertTask.getAppName())){
                        alertFaultAndAlertTask.setMonitorMarket(jsonObject.getString(alertFaultAndAlertTask.getAppName()));
                    }
                });
            }
            PageInfo pageInfo = new PageInfo<>(list);
            return new Result(RspCode.SUCCESS,null,new PageVO<AlertFaultAndAlertTask>(pageInfo.getList(), pageInfo.getPages(),pageInfo.getTotal()));
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("getAlertProcessList error , param is:" + pageInfoVO,e);
            return new Result(RspCode.EXCEPTION);
        }
    }
}
