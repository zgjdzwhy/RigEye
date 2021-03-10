package com.rigai.rigeye.common.constant;

import com.rigai.rigeye.common.exception.ParamException;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/20.
 * 用于变量取值类型描述的获取
 */

public class ValueTypeDescription {
    public static String getValueTypeDescription(Integer valueType){
        switch (valueType){
            case 1:
                return "总和";
            case 2:
                return "平均值";
            case 3:
                return "最小值";
            case 4:
                return "最大值";
            default:
                throw new ParamException("{参数错误，报警规则取值类型异常，仅允许1-4,实际值:"+valueType+"}");
        }
    }
}
