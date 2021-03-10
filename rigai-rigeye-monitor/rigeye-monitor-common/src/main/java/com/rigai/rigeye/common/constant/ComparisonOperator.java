package com.rigai.rigeye.common.constant;

import com.rigai.rigeye.common.exception.ParamException;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/20.
 * 用于定义变量取值操作符的转换
 */

public class ComparisonOperator {
    public static String getComparisonOperator(Integer valueType){
        switch (valueType){
            case 1:
                return ">=";
            case 2:
                return "<=";
            case 3:
                return "=";
            case 4:
                return "!=";
            default:
                throw new ParamException("{参数错误，报警规则取值类型异常，仅允许1-4,实际值:"+valueType+"}");
        }
    }

    public static boolean compare(long threshold,Integer valueType,double value){
        switch (valueType){
            case 1:
                return value>=threshold;
            case 2:
                return value<=threshold;
            case 3:
                long temp=(long) value;
                return temp==threshold;
            case 4:
              return value!=threshold;
            default:
                throw new ParamException("{参数错误，报警规则取值类型异常，仅允许1-4,实际值:"+valueType+"}");
        }
    }

}
