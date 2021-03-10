package com.rigai.rigeye.web.controller;

import com.dianping.cat.Cat;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.bean.vo.DataSetAggrateParamVO;
import com.rigai.rigeye.web.bean.vo.ExceptionLogParamVO;
import com.rigai.rigeye.web.bean.vo.LogDetailParamVO;
import com.rigai.rigeye.web.service.DetailSearchService;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.rigai.rigeye.web.bean.vo.TaskRunDetailParamVO;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author yh
 * @date 2018/9/4 16:50
 */
@RestController
@RequestMapping("/api/detailSearch")
public class DetailSearchController {
    private Logger logger= LoggerFactory.getLogger(DetailSearchController.class);

    @Autowired
    DetailSearchService detailSearchService;

    /**
     * 查询任务切分详情
     * @param taskRunDetailParamVO
     * @param bindingResult
     * @return
     */
    @PostMapping("/taskRunDetail")
    public Result searchTaskRunDetail(@RequestBody TaskRunDetailParamVO taskRunDetailParamVO, BindingResult bindingResult){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("searchTaskRunDetail wrong param {} ",taskRunDetailParamVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }

        try{
            return detailSearchService.searchTaskRunDetail(taskRunDetailParamVO);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("searchTaskRunDetail error,param :"+taskRunDetailParamVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    @PostMapping("/badDivide")
    public Result searchBadDivideDetail(@RequestBody TaskRunDetailParamVO taskRunDetailParamVO, BindingResult bindingResult){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("searchTaskRunDetail wrong param {} ",taskRunDetailParamVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }

        try{
            return detailSearchService.searchBadDivideDetail(taskRunDetailParamVO);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("searchBadDivideDetail error,param :"+taskRunDetailParamVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    @PostMapping("/exceptionLogList")
    public Result searchExceptionLogList(@Valid @RequestBody ExceptionLogParamVO exceptionLogParamVO, BindingResult bindingResult){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("searchExceptionLogList wrong param {} ",exceptionLogParamVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try{
            return detailSearchService.searchExceptionLogList(exceptionLogParamVO);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("searchExceptionLogList error,param :"+exceptionLogParamVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    @PostMapping("/logDetailList")
    public Result searchLogDetailList(@Valid @RequestBody LogDetailParamVO logDetailParamVO, BindingResult bindingResult){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("logDetailParamVO wrong param {} ",logDetailParamVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try{
            return detailSearchService.searchLogDetail(logDetailParamVO);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("logDetailParamVO error,param :"+logDetailParamVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查询数据集聚合详情
     * @return
     */
    @PostMapping("/dataSetAggDetail")
    public Result searchDataSetAggrationDetail(@Valid @RequestBody DataSetAggrateParamVO dataSetAggrateParamVO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            logger.info("searchDataSetAggrationDetail wrong param. {0}",dataSetAggrateParamVO);
            return new Result(RspCode.PARAMS_ERROR, null);
        }

        try{
            return detailSearchService.searchDataSetAggrationDetail(dataSetAggrateParamVO);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("searchDataSetAggrationDetail error,param :" + dataSetAggrateParamVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }
}
