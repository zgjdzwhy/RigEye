package com.rigai.rigeye.web.controller;


import com.rigai.rigeye.alert.bean.AlertTaskInfo;
import com.rigai.rigeye.alert.service.AlertApiService;
import com.rigai.rigeye.common.model.AlertTask;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.rigai.rigeye.web.bean.vo.AlertTaskSearchVO;
import com.rigai.rigeye.web.bean.vo.AlertTaskVO;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/20.
 */

@RestController
@RequestMapping("/alert/task")
public class AlertTaskController {

    private Logger logger= LoggerFactory.getLogger(AlertTaskController.class);

    @Autowired
    AlertApiService alertApiService;


    @PostMapping("/add")
    public Result add(@RequestBody @Valid AlertTaskInfo alertTaskInfo, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("add alert task wrong param {} ", fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        alertApiService.addAlertTask(alertTaskInfo.getTask(),alertTaskInfo.getRules(),userId);
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/list")
    public Result list(@RequestBody @Valid PageInfoVO<AlertTaskSearchVO> pageInfoVO, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if (bindingResult.hasErrors()) {
            FieldError fieldError= bindingResult.getFieldError();
            logger.info("list alert task wrong param {} ", fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        String trueUser=null;
        if(request.getAttribute(BaseConstant.TRUE_USER_KEY)!=null){
            trueUser=(String)request.getAttribute(BaseConstant.TRUE_USER_KEY);
        }
        AlertTask param=new AlertTask();
        param.setUserId(userId);
        if(pageInfoVO.getInfo()!=null){
            param.setAppName(pageInfoVO.getInfo().getAppName());
            param.setName(pageInfoVO.getInfo().getAlertName());
        }
        PageInfo<AlertTaskInfo> alertTaskPageInfo=alertApiService.pageGetAlertTask(pageInfoVO.getPage(),pageInfoVO.getPageSize(),param,trueUser);
        boolean empty=!(alertTaskPageInfo!=null&&alertTaskPageInfo.getList()!=null&&alertTaskPageInfo.getList().size()>0);
        if(empty){
            return new Result(RspCode.NO_RESULT);
        }
        List<AlertTaskVO> voList=AlertTaskVO.toAlertTaskVO(alertTaskPageInfo.getList());
        Collections.sort(voList);
        PageVO<AlertTaskVO> vo=new PageVO<>(voList,alertTaskPageInfo.getPages(),alertTaskPageInfo.getTotal());
        Result result=new Result(RspCode.SUCCESS);
        result.setData(vo);
        return result;
    }

    @PostMapping("/delete")
    public Result delete(Long id, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if (id==null||id<1) {
            logger.info("delete alert task wrong param {} ", id);
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        boolean result=alertApiService.deleteAlertTask(id,userId);
        if(!result){
            return new Result(RspCode.EXCEPTION);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody @Valid AlertTaskInfo alertTaskInfo,BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if (bindingResult.hasErrors()||alertTaskInfo.getTask().getId()==null||alertTaskInfo.getTask().getId()<1) {
            FieldError fieldError = bindingResult.getFieldError();
            logger.info("edit alert task wrong param field name:{} get vale:{} rule: {}", fieldError.getField(),fieldError.getRejectedValue(),fieldError.getDefaultMessage());
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        boolean success=alertApiService.editAlertTask(alertTaskInfo.getTask(), alertTaskInfo.getRules(),userId);
        if (!success){
            return new Result(RspCode.NO_RESULT);
        }
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/detail")
    public Result detail(Long id,HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if (id==null||id<1) {
            logger.info("get alert task detail wrong param {} ", id);
            return new Result(RspCode.PARAMS_ERROR, null);
        }
        String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
        AlertTaskInfo data=alertApiService.getAlertTask(id,userId);
        if(data==null){
            return new Result(RspCode.NO_RESULT);
        }
        Result result=new Result(RspCode.SUCCESS);
        result.setData(data);
        return result;
    }

    @PostMapping("/all")
    public Result getAllAlertName(){
        List<AlertTask> all=alertApiService.getAll();
        Result result=new Result(RspCode.SUCCESS);
        result.setData(all);
        return result;
    }


    @PostMapping("/start")
    public Result startTask(Long taskId){
        if(taskId==null||taskId<0){
            return new Result(RspCode.PARAMS_ERROR);
        }
        alertApiService.startTask(taskId);
        return new Result(RspCode.SUCCESS);
    }

    @PostMapping("/stop")
    public Result stopTask(Long taskId){
        if(taskId==null||taskId<0){
            return new Result(RspCode.PARAMS_ERROR);
        }
        alertApiService.stopTask(taskId);
        return new Result(RspCode.SUCCESS);
    }
}
