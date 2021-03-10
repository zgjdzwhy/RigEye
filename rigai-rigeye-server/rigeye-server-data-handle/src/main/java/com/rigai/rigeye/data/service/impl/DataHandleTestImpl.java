package com.rigai.rigeye.data.service.impl;

import com.rigai.rigeye.common.model.AlertFaultProcessor;
import com.rigai.rigeye.common.model.DataTask;
import com.rigai.rigeye.common.service.AlertFaultProcessorService;
import com.rigai.rigeye.common.service.DataTaskService;
import com.rigai.rigeye.data.service.DataHandleTest;
import com.em.fx.core.mybatis.bean.Sort;
import com.em.fx.core.mybatis.complexQuery.CustomQueryParam;
import com.em.fx.core.mybatis.complexQuery.WithValueQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/7/31.
 */
@Component
public class DataHandleTestImpl implements DataHandleTest {

    @Autowired
    AlertFaultProcessorService alertFaultProcessorService;
    @Autowired
    DataTaskService dataTaskService;
    @Override
    public String test() {
        AlertFaultProcessor alertFaultProcessor=new AlertFaultProcessor();
        alertFaultProcessor.setAlertNum(10);
        alertFaultProcessor.setAlertDate(new Date());
        alertFaultProcessor.setStatus(1);
        alertFaultProcessor.setAppName("aaa");
        alertFaultProcessor.setTriggerRuleDesc("aaa");
        alertFaultProcessor.setAlertTaskId(1);
        alertFaultProcessorService.insert(alertFaultProcessor);
        System.out.println(alertFaultProcessor);
        return alertFaultProcessor.toString();
    }

    @Override
    public String test1(){
        DataTask dataTask = new DataTask();
        dataTask.setAppName("ook");
        System.out.println("test1");
        CustomQueryParam customQueryParam = new WithValueQueryParam("appName","=","卡");
        Sort sort = new Sort();
        sort.setProperty("id");
        sort.setProperty("asc");
        List query = new ArrayList();
        query.add(customQueryParam);
        List sorts = new ArrayList();
        sorts.add(sort);

        return "hello word!";
    }
}
