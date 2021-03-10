package com.rigai.rigeye.web.controller;

import com.dianping.cat.Cat;
import com.rigai.rigeye.common.dto.DerivedMeasure;
import com.rigai.rigeye.common.dto.Dimension;
import com.rigai.rigeye.common.dto.Filter;
import com.rigai.rigeye.common.dto.Measure;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.model.DataTask;
import com.rigai.rigeye.common.service.DataSetRuleService;
import com.rigai.rigeye.common.service.DataTaskService;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.bean.vo.AlertVariableDefineVO;
import com.rigai.rigeye.web.bean.vo.DataSetModelVO;
import com.rigai.rigeye.web.bean.vo.DataSetVO;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.rigai.rigeye.web.service.DataSetHandleService;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * func:数据集管理
 * @author yh
 * @date 2018/8/10 11:04
 */
@RestController
public class DataSetController {
    private Logger logger= LoggerFactory.getLogger(DataSetController.class);
    @Autowired
    private DataSetRuleService dataSetRuleService;
    @Autowired
	DataSetHandleService dataSetHandleService;
    @Autowired
    DataTaskService dataTaskService;

    /**
     * 保存和更新数据集
     * @param dataSetRule
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/dataset/save")
    public Result saveOrUpdate(@Valid @RequestBody DataSetRule dataSetRule, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("save or update dataTask wrong param {} ",dataSetRule);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        //验证筛选条件
        List<Filter> filters = dataSetRule.getDatasetRule().getFilters();
        String filterOperation = dataSetRule.getDatasetRule().getFilterOperator();
        if (filters != null && filters.size() > 0){
            boolean f = BaseConstant.OR.equals(filterOperation) || BaseConstant.AND.equals(filterOperation);
            if (!f){
                logger.info("save or update dataTask wrong param {} ",dataSetRule);
                return new Result(RspCode.PARAMS_ERROR,"筛选符号错误，请选择：| or &");
            }
        }
        try{
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            dataSetRule.setUserId(userId);
            return dataSetHandleService.saveOrUpdate(dataSetRule);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("save or update dataTask error , param is:" + dataSetRule,e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 分页查询，可带参数
     * @param pageInfoVO
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/dataset/find")
    public Result getAll(@Valid @RequestBody PageInfoVO<DataSetVO> pageInfoVO, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("get dataset wrong param {} ",pageInfoVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try {
            //查询条件
            DataSetRule rule = new DataSetRule();
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            rule.setUserId(userId);
            if (StringUtils.isNotEmpty(pageInfoVO.getInfo().getName())){
                if (StringUtils.isNotBlank(pageInfoVO.getInfo().getName())) {
                    rule.setName("%" + pageInfoVO.getInfo().getName() + "%");
                }
            }
            PageInfo<DataSetRule> rules = dataSetRuleService.selectByPage(rule,pageInfoVO.getPage(),pageInfoVO.getPageSize());
            List<DataTask> dataTasks = rules.getList().parallelStream().map(DataSetRule::getTaskId).distinct().map(aLong -> dataTaskService.getById(aLong)).collect(Collectors.toList());
            rules.getList().forEach(dataSetRule -> {
                dataTasks.forEach(dataTask -> {
                    if (dataTask.getId().equals(dataSetRule.getTaskId())){
                        dataSetRule.setTaskName(dataTask.getTaskName());
                    }
                });
            });
            Result result = new Result(RspCode.SUCCESS);
            PageVO pageVO = new PageVO(rules.getList(), rules.getPages(), rules.getTotal());
            result.setData(pageVO);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get dataset error ,param is : "+pageInfoVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查询数据集
      * @param id
     * @param request
     * @return
     */
    @GetMapping("/api/dataset/{id}")
    public Result getById(@PathVariable Long id, HttpServletRequest request){
        try{
            Result result = new Result(RspCode.SUCCESS);
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            DataSetRule rule = new DataSetRule();
            rule.setUserId(userId);
            rule.setId(id);
            List<DataSetRule> dataSetRules = dataSetRuleService.getByObj(rule);
            if (dataSetRules != null && dataSetRules.size() >0 ){
                result.setData(dataSetRules.get(0));
            }
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get dataset error ,param is : "+id, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 根据任务id查询数据集列表
     * @param taskId
     * @return
     */
    @GetMapping("/api/dataset/findByTaskId/{taskId}")
    public Result getByTaskId(@PathVariable Long taskId, HttpServletRequest request){
        try{
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            Result result = new Result(RspCode.SUCCESS);
            DataTask dataTask = dataTaskService.getById(taskId);
            if ((dataTask != null) && dataTask.getUserId().equals(userId)) {
                result.setData(dataSetHandleService.getDataSetChecks(taskId));
            }
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("dataset findByTaskId error ,param is : "+taskId, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 删除数据集
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/api/dataset/{id}")
    public Result deleteById(@PathVariable Long id, HttpServletRequest request){
        try{
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            //删除任务绑定的数据集列表
            DataSetRule rule = new DataSetRule();
            rule.setUserId(userId);
            rule.setId(id);
            dataSetRuleService.delete(rule);
            return new Result(RspCode.SUCCESS);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("delete dataset error ,param is : "+id, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查询数据集模型可以进行的各种操作
     * @param model
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/dataset/chartConfig")
    public Result getChartConfig(@Valid @RequestBody DataSetModelVO model, BindingResult bindingResult){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("dataset chartConfig wrong param {} ",model);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try{
            Result result = new Result(RspCode.SUCCESS);
            result.setData(dataSetHandleService.chartConfig(model));
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("dataset chartConfig error ,param is : " + model, e);
            return new Result(RspCode.EXCEPTION);
        }
    }


    /**
     * 查询数据集作为报警变量
     * @param request
     * @return
     */
    @GetMapping("/api/dataset/getVariable")
    public Result getAllDataSetAsAlertVariable(HttpServletRequest request){
        try{
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            DataSetRule rule = new DataSetRule();
            rule.setUserId(userId);
            List<DataSetRule> dataSetRules = dataSetRuleService.getByObj(rule);
            if (dataSetRules == null || dataSetRules.size() < 1){
                return new Result(RspCode.SUCCESS);
            }

            List<AlertVariableDefineVO> alertVariableDefineVOS = dataSetRules.parallelStream().map(dataSetRule -> {
                DataTask dataTask = dataTaskService.getById(dataSetRule.getTaskId());
                AlertVariableDefineVO alertVariableDefineVO = new AlertVariableDefineVO();
                alertVariableDefineVO.setTaskId(dataTask.getId());
                alertVariableDefineVO.setTaskName(dataTask.getTaskName());
                //设置数据集在influx db中的表名称，后续查询需要使用
                alertVariableDefineVO.setTableId(BaseConstant.INFLUX_DATA_SET_TABLE_PREFIX + dataSetRule.getId());
                alertVariableDefineVO.setId(dataSetRule.getId());
                alertVariableDefineVO.setName(dataSetRule.getName());
                alertVariableDefineVO.setCreateTime(dataSetRule.getCreateTime());
                alertVariableDefineVO.setUpdateTime(dataSetRule.getUpdateTime());
                alertVariableDefineVO.setType(dataSetRule.getType());
                alertVariableDefineVO.setStatus(dataSetRule.getStatus());

                List<Measure> measures = dataSetRule.getDatasetRule().getMeasures();
                List<DerivedMeasure> derivedMeasures = dataSetRule.getDatasetRule().getDerivedMeasures();
                List<String> measuresList = new ArrayList<>();
                if (measures != null){
                    measuresList.addAll(measures.parallelStream().map(Measure::getMeasureLabel).collect(Collectors.toList()));
                }
                if (derivedMeasures != null){
                    measuresList.addAll(derivedMeasures.parallelStream().map(DerivedMeasure::getMeasureLabel).collect(Collectors.toList()));
                }
                alertVariableDefineVO.setMeasures(measuresList);
                List<Dimension> dimensions = dataSetRule.getDatasetRule().getDimensions();
                if (dimensions != null){
                    alertVariableDefineVO.setDimensions(dimensions.parallelStream().map(Dimension::getDimension).collect(Collectors.toList()));
                }
                List<Dimension> optionalDims = dataSetRule.getDatasetRule().getOptionalDims();
                if (optionalDims != null){
                    alertVariableDefineVO.setOptionalDims(optionalDims.parallelStream().map(Dimension::getDimension).collect(Collectors.toList()));
                }
                return alertVariableDefineVO;
            }).collect(Collectors.toList());
            return new Result(RspCode.SUCCESS,null, alertVariableDefineVOS);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("dataset getAllDataSetAsAlertVariable error. ", e);
            return new Result(RspCode.EXCEPTION);
        }
    }
}
