package com.rigai.rigeye.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.model.ExceptionContent;
import com.rigai.rigeye.common.service.ExceptionContentService;
import com.rigai.rigeye.web.bean.vo.ExceptionContentVO;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.em.fx.redis.dao.RedisDao;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/27.
 */

@RestController
@RequestMapping("/exception/content")
public class ExceptionContentController {

    private Logger logger= LoggerFactory.getLogger(ExceptionContentController.class);

    @Autowired
    ExceptionContentService exceptionContentService;
    @Autowired
    RedisDao redisDao;

    @PostMapping("/add")
    public Result add(@RequestBody @Valid ExceptionContentVO exceptionContentVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("add exceptionContent wrong param {}",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        ExceptionContent exceptionContent=new ExceptionContent();
        BeanUtils.copyProperties(exceptionContentVO,exceptionContent);
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        exceptionContent.setUserId(userId);
        try{
            exceptionContentService.insert(exceptionContent);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_CONTENT, JSONArray.toJSONString(exceptionContentService.getAll()));
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("exceptionContent insert error param:"+exceptionContentVO.toString(),e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody @Valid ExceptionContentVO exceptionContentVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()||exceptionContentVO.getId()==null) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("edit exceptionContent wrong param {}",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        ExceptionContent exceptionContent=new ExceptionContent();
        BeanUtils.copyProperties(exceptionContentVO,exceptionContent);
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        exceptionContent.setUserId(userId);
        try{
            exceptionContentService.update(exceptionContent);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_CONTENT, JSONArray.toJSONString(exceptionContentService.getAll()));
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("exceptionContent edit error param:"+exceptionContentVO.toString(),e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/delete")
    public Result delete(Integer id, HttpServletRequest request){
        if (id==null||id<0) {
            logger.info("delete exceptionContent wrong param {} ", id);
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        ExceptionContent exceptionContent=new ExceptionContent();
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        exceptionContent.setUserId(userId);
        exceptionContent.setId(id);
        try{
            exceptionContentService.deleteExceptionContent(exceptionContent);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_CONTENT, JSONArray.toJSONString(exceptionContentService.getAll()));
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("exceptionContent delete error param:"+id.toString(),e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }


    @PostMapping("/list")
    public Result getAll(HttpServletRequest request){
        List<ExceptionContent> contents;
        try{
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            ExceptionContent param=new ExceptionContent();
            param.setUserId(userId);
            contents=exceptionContentService.getByObj(param);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("exceptionContent get error ",e);
            return new Result(RspCode.EXCEPTION);
        }
        Result result=new Result(RspCode.SUCCESS);
        result.setData(contents);
        return result;
    }

}
