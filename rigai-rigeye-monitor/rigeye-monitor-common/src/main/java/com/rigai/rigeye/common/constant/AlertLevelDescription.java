package com.rigai.rigeye.common.constant;

import com.rigai.rigeye.common.exception.ParamException;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/20.
 */

public class AlertLevelDescription {

    private static final String L5="提示";
    private static final String L4="普通";
    private static final String L3="警告";
    private static final String L2="错误";
    private static final String L1="致命";

    public static String getDescription(String alertLevel){
        switch (alertLevel){
            case "L1":
                return L1;
            case "L2":
                return L2;
            case "L3":
                return L3;
            case "L4":
                return L4;
            case "L5":
                return L5;
            default:
                throw new ParamException("{参数错误，报警级别仅允许L1/L2/L3/L4/L5,实际值:"+alertLevel+"}");
        }
    }
}
