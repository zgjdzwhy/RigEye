package com.rigai.rigeye.alert.service.impl;

import com.rigai.rigeye.alert.service.AlertTest;
import com.rigai.rigeye.common.constant.CommonConstant;
import org.springframework.stereotype.Component;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/7/31.
 */

@Component
public class AlertTestImpl implements AlertTest {
    @Override
    public String test() {
        String test= CommonConstant.SPLIT_SIGN+"alert";
        System.out.println(test);
        return test;
    }
}
