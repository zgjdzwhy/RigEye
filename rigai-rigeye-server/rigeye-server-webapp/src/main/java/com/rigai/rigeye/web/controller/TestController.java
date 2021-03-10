package com.rigai.rigeye.web.controller;

import com.rigai.rigeye.alert.service.AlertTest;
import com.rigai.rigeye.data.service.DataHandleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/7/31.
 */
@Controller
public class TestController {
    @Autowired
    DataHandleTest dataHandleTest;
    @Autowired
    AlertTest alertTest;

    @RequestMapping("/test")
    public @ResponseBody String test(){
        System.out.println("==============");
        return dataHandleTest.test()+"=="+alertTest.test();
    }

    @RequestMapping("/test1")
    public @ResponseBody String test1(){
        System.out.println("==============");
        return dataHandleTest.test1();
    }
}
