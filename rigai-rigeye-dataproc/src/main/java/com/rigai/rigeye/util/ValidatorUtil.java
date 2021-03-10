package com.rigai.rigeye.util;

import com.rigai.rigeye.model.DataTask;
import com.rigai.rigeye.service.impl.DataProcServiceImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.*;

/**
 * @author yh
 * @date 2018/9/3 10:43
 */
public class ValidatorUtil implements Serializable {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public static <T> ValidatorResult validate(T obj){
        ValidatorResult result = new ValidatorResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj,Default.class);
        if(set != null && set.size() > 0){
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<>(4);
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static <T> ValidatorResult validateProperty(T obj,String propertyName){
        ValidatorResult result = new ValidatorResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj,propertyName,Default.class);
        if(set != null && set.size() > 0){
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<>(4);
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    public static void main(String[] a){
        DataTask dataTask = DataProcServiceImpl.INSTANCE.getOne(2L);
        ValidatorResult result = validate(dataTask);
    }
}
