package com.rigai.rigeye.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.bean.ExceptionFilterListDTO;
import com.rigai.rigeye.common.model.ExceptionFilter;
import com.rigai.rigeye.common.service.ExceptionFilterService;
import com.rigai.rigeye.web.bean.vo.ExceptionFilterVO;
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
 *         Created by ChenXing on 2018/8/28.
 */
@RestController
@RequestMapping("/exception/filter")
public class ExceptionFilterController {

    private Logger logger= LoggerFactory.getLogger(ExceptionFilterController.class);

    @Autowired
    ExceptionFilterService exceptionFilterService;
    @Autowired
    RedisDao redisDao;

    @PostMapping("/add")
    public Result add(@RequestBody @Valid ExceptionFilterVO exceptionFilterVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("add ExceptionFilter wrong param {}",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        ExceptionFilter exceptionFilter=new ExceptionFilter();
        BeanUtils.copyProperties(exceptionFilterVO,exceptionFilter);
        exceptionFilter.setUserId(userId);
        try{
            exceptionFilterService.insert(exceptionFilter);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_FILTER, JSONArray.toJSONString(exceptionFilterService.getAll()));
        }catch (Exception e){
            logger.error("add exceptionFilter error param:{} ,exception is {} ",exceptionFilterVO, ExceptionUtils.getStackTrace(e));
            Cat.logError("add exceptionFilter error param: "+exceptionFilterVO,e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody @Valid ExceptionFilterVO exceptionFilterVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()||exceptionFilterVO.getId()==null) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("edit ExceptionFilter wrong param {}",fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        ExceptionFilter exceptionFilter=new ExceptionFilter();
        BeanUtils.copyProperties(exceptionFilterVO,exceptionFilter);
        exceptionFilter.setUserId(userId);
        try{
            exceptionFilterService.update(exceptionFilter);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_FILTER, JSONArray.toJSONString(exceptionFilterService.getAll()));
        }catch (Exception e){
            logger.error("update exceptionFilter error param:{} ,exception is {} ",exceptionFilterVO, ExceptionUtils.getStackTrace(e));
            Cat.logError("update exceptionFilter error param: "+exceptionFilterVO,e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/list")
    public Result list(HttpServletRequest request){
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        List<ExceptionFilterListDTO> list=exceptionFilterService.getList(userId);
        Result result=new Result(RspCode.SUCCESS);
        result.setData(list);
        return result;
    }

    @PostMapping("/delete")
    public Result delete(Integer id,HttpServletRequest request){
        if (id==null||id<0) {
            logger.info("delete exception filter wrong param {} ", id);
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        ExceptionFilter exceptionFilter=new ExceptionFilter();
        exceptionFilter.setUserId(userId);
        exceptionFilter.setId(id);
        try{
            exceptionFilterService.delete(exceptionFilter);
            //更新redis配置列表
            redisDao.setStr(BaseConstant.EXCEPTION_FILTER, JSONArray.toJSONString(exceptionFilterService.getAll()));
        }catch (Exception e){
            logger.error("delete exceptionFilter error param:{} ,exception is {} ",exceptionFilter, ExceptionUtils.getStackTrace(e));
            Cat.logError("delete exceptionFilter error param: "+exceptionFilter,e);
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

}
