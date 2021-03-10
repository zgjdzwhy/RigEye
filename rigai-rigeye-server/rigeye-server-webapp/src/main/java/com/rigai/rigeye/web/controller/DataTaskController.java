package com.rigai.rigeye.web.controller;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.model.ContactsGroup;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.model.DataTask;
import com.rigai.rigeye.common.service.*;
import com.rigai.rigeye.data.service.impl.SparkEngineImpl;
import com.rigai.rigeye.web.bean.common.PageInfoVO;
import com.rigai.rigeye.web.bean.common.PageVO;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.bean.vo.*;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.rigai.rigeye.web.service.DataSetHandleService;
import com.rigai.rigeye.web.service.DataTaskHandleService;
import com.em.fx.blockly.smartsplit.SmartParseLog;
import com.em.fx.blockly.xmlsplit.BlocklyXmlParseLog;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.em.fx.redis.dao.RedisDao;
import com.em.fx.utils.date.DateUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yh
 * @date 2018/8/3 14:32
 */
@RestController
public class DataTaskController {
    @Autowired
    AlertFaultProcessorService alertFaultProcessorService;
    @Autowired
    DataTaskService dataTaskService;
    @Autowired
    DataSetRuleService dataSetRuleService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    DevideModelService devideModelService;
    @Autowired
    ContactsGroupService contactsGroupService;
    @Autowired
    DataSetHandleService dataSetHandleService;
    @Autowired
    SparkEngineImpl sparkEngine;
    @Autowired
    RedisDao redisDao;
    @Autowired
    DataTaskHandleService dataTaskHandleService;


    private Logger logger= LoggerFactory.getLogger(DataTaskController.class);
    /**
     * 新建
     * @param task
     * @return
     */
    @PostMapping("/api/task/save")
    public Result createDataTask(@RequestBody Map<String, Object> task, HttpServletRequest request){
        String taskName = (String) task.get("taskName");
        String appName = (String) task.get("appName");
        if (StringUtils.isNotEmpty(taskName) && StringUtils.isNotEmpty(appName)){
            DataTask dataTask = new DataTask();
            try {
                String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
                dataTask.setTaskName(taskName);
                dataTask.setUserId(userId);
                List<DataTask> dataTasks = dataTaskService.getByObj(dataTask);
                if (dataTasks != null && dataTasks.size()> 0){
                    Result result = new Result(RspCode.EXCEPTION);
                    result.setMessage("监控任务名称已存在");
                    return result;
                }

                dataTask.init();
                dataTask.setTaskName(taskName);
                dataTask.setAppName(appName);

                dataTask.setUserId(userId);
                dataTaskService.insert(dataTask);
                Result result = new Result(RspCode.SUCCESS);
                Map map = new HashMap();
                map.put("id",dataTask.getId());
                result.setData(map);
                return result;
            }catch (Exception e){
                logger.error(ExceptionUtils.getStackTrace(e));
                Cat.logError("save dataTask error , param is:" + task,e);
                return new Result(RspCode.EXCEPTION);
            }
        }else {
            return new Result(RspCode.PARAMS_ERROR);
        }
    }

    /**
     * 更新
     * @param task
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/task/update")
    public Result updateDataTask(@Valid @RequestBody DataTaskParamVO task,  BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("update dataTask wrong param {} ",task);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        boolean f = StringUtils.isEmpty(task.getLogSample()) ||
                StringUtils.isEmpty(task.getLogAnalysisXml()) ||
                task.getCleanType() == null ||
                ((task.getCleanType() != 1) && (task.getCleanType() != 2));

        /* 数据集页面的保存和完成配置参数 */
        boolean third = (task.getStep() > 1) && (f || task.getDevideModel() == null);
        if (third){
            logger.info("update dataTask wrong param {} ", task);
            return new Result(RspCode.PARAMS_ERROR, null);
        }

        try {
            DataTask dataTask = dataTaskService.getById(task.getId());
            if (dataTask == null){
                logger.info("update dataTask ,but not find param {} ",task);
                return new Result(RspCode.NO_RESULT,null);
            }
            BeanUtils.copyProperties(task, dataTask);

            if (task.getStep() != 1 && task.getDevideModel() != null) {
                //第二步和第三步 保存切分模型
                dataTask.setModel(JSON.toJSONString(task.getDevideModel()));
            }

            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            dataTask.setUserId(userId);
            dataTask.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            dataTaskService.update(dataTask);
            return new Result(RspCode.SUCCESS);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("update dataTask error , param is:" + task,e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @GetMapping("/api/task/{id}")
    public Result getDataTaskById(@PathVariable Long id, HttpServletRequest request){
        try{
            //查询监控任务
            DataTask param = new DataTask();
            param.setId(id);
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            param.setUserId(userId);
            List<DataTask> dataTasks = dataTaskService.getByObj(param);
            if (dataTasks != null && dataTasks.size() > 0){
                DataTask dataTask = dataTasks.get(0);
                DataTaskDetailVO dataTaskDetailVO = new DataTaskDetailVO();
                BeanUtils.copyProperties(dataTask, dataTaskDetailVO);

                //查询任务绑定的数据源
                DataSourceDTO dataSource = dataSourceService.getDataSourceDTOById(dataTask.getDataSourceId().longValue());
                if (dataSource != null){
                    DataSourceVO dataSourceVO = new DataSourceVO();
                    BeanUtils.copyProperties(dataSource, dataSourceVO);
                    dataTaskDetailVO.setDataSource(dataSourceVO);
                }
                //设置明细数据落地数据源
                DataSourceDTO dest = dataSourceService.getDataSourceDTOById(dataTask.getDataSinkId().longValue());
                if (dest != null){
                    DataSourceVO dataSourceVO = new DataSourceVO();
                    BeanUtils.copyProperties(dest, dataSourceVO);
                    dataTaskDetailVO.setDestDataSource(dataSourceVO);
                }

                //查询任务绑定的数据集列表
                List<DataSetCheckVO> dataSetCheckVOS = dataSetHandleService.getDataSetChecks(id);
                dataTaskDetailVO.setDataSets(dataSetCheckVOS);
                Result result = new Result(RspCode.SUCCESS);
                result.setData(dataTaskDetailVO);
                return result;
            }else {
                Result result = new Result(RspCode.SUCCESS);
                return result;
            }
        }catch (Exception e){
            Result result = new Result(RspCode.EXCEPTION);
            return result;
        }
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/api/task/{id}")
    public Result deleteDataTaskById(@PathVariable Long id, HttpServletRequest request){
        try{
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            return dataTaskHandleService.deleteOne(id, userId);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("delete dataTask error ,id = "+id, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 根据参数，分页查询
     * @param pageInfoVO
     * @param bindingResult
     * @return
     */
    @PostMapping("/api/task/find" )
    public Result pageGetDataTasks(@Valid @RequestBody PageInfoVO<DataTask> pageInfoVO, BindingResult bindingResult, HttpServletRequest request){
        //如果绑定结果有误则返回参数错误
        if(bindingResult.hasErrors()){
            logger.info("get dataTasks wrong param {} ",pageInfoVO);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try {
            //查询条件
            DataTask query = new DataTask();
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            String trueUser=null;
            if(request.getAttribute(BaseConstant.TRUE_USER_KEY)!=null){
                trueUser=(String)request.getAttribute(BaseConstant.TRUE_USER_KEY);
            }
            query.setUserId(userId);
            query.setIsDel("0");
            if (pageInfoVO.getInfo() != null){
                if (StringUtils.isNotBlank(pageInfoVO.getInfo().getTaskName())) {
                    query.setTaskName("%" + pageInfoVO.getInfo().getTaskName() + "%");
                }
                if(StringUtils.isNotBlank(pageInfoVO.getInfo().getAppName())){
                    query.setAppName("%" + pageInfoVO.getInfo().getAppName() + "%");
                }
                boolean f = (pageInfoVO.getInfo().getMonitorStatus() != null) &&
                        (1 == pageInfoVO.getInfo().getMonitorStatus() || 2 == pageInfoVO.getInfo().getMonitorStatus());
                if (f){
                    query.setMonitorStatus(pageInfoVO.getInfo().getMonitorStatus());
                }
            }
            PageInfo<DataTask> dataTasks = dataTaskService.pageFindList(query,pageInfoVO.getPage(),pageInfoVO.getPageSize(),trueUser);
            List<DataTask> dataTaskList = dataTasks.getList();
            List<DataTaskVO> dataTaskVOS = null;
            if (dataTaskList != null){
                dataTaskVOS = dataTaskList.parallelStream().map(dataTask -> {
                    DataTaskVO dataTaskVO = new DataTaskVO();
                    BeanUtils.copyProperties(dataTask, dataTaskVO);
                    DataSetRule rule = new DataSetRule();
                    rule.setTaskId(dataTask.getId());
                    dataTaskVO.setDataSet(dataSetRuleService.count(rule));
                    return dataTaskVO;
                }).collect(Collectors.toList());
            }
            Result result = new Result(RspCode.SUCCESS);
            PageVO<DataTaskVO> pageVO = new PageVO(dataTaskVOS, dataTasks.getPages(), dataTasks.getTotal());
            result.setData(pageVO);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get dataTasks error ,param is : "+pageInfoVO, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 自定义切分预览
     * @param param
     * @return
     */
    @PostMapping("/api/task/clearPreview")
    public Result clearPreview(@RequestBody Map<String, Object> param){
        String log = (String) param.get("logSample");
        String xml = (String) param.get("logAnalysisXml");
        if (log == null || StringUtils.isEmpty(xml)){
            logger.info("clearPreview wrong param {} ",param);
            return new Result(RspCode.PARAMS_ERROR,null);
        }else {
            try{
                List list = new ArrayList(8);
                BlocklyXmlParseLog blocklyXmlParseLog = new BlocklyXmlParseLog(xml);
                for (String s : log.split("\n")) {
                    Map res = blocklyXmlParseLog.parseLog(s);
                    list.add(res);
                }
                Result result = new Result(RspCode.SUCCESS);
                result.setData(list);
                return result;
            }catch (Exception e){
                logger.error(ExceptionUtils.getStackTrace(e));
                Cat.logError("clearPreview error , param is:" + param,e);
                return new Result(RspCode.EXCEPTION,"数据切分异常，请检查切分模型是否正确！");
            }
        }
    }

    /**
     * 智能切分
     * @param param
     * @return
     */
    @PostMapping("/api/task/smartSplit")
    public Result smartSplit(@RequestBody Map<String, Object> param){
        String log = (String) param.get("logSample");
        if (log == null){
            logger.info("clearPreview wrong param {} ",param);
            return new Result(RspCode.PARAMS_ERROR,null);
        }
        try {
            List list = new ArrayList(8);
            SmartParseLog smartParseLog = new SmartParseLog();
            String[] logs = log.split("\n");
            Map sample = smartParseLog.smartParser(logs[0].trim());
            list.add(sample);
            Result result = new Result(RspCode.SUCCESS);
            result.setData(list);
            return result;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("smartSplit error , param is:" + param, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查找新建的任务名称和它绑定的项目名称
     * @return
     */
    @GetMapping("/api/task/findTaskNameAndAppName")
    public Result findTaskNameAndAppName(HttpServletRequest request){
        try{
            Result result = new Result(RspCode.SUCCESS);
            String userId=(String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            List<String> taskNames = dataTaskService.findDataTaskNameByUserId(userId);
            List<String> appNames = dataTaskService.findAppNameByUserId(userId);
            Map<String,List<String>> map = new HashMap<>(8);
            map.put("taskName",taskNames);
            map.put("appName",appNames);
            result.setData(map);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get findTaskNameAndAppName error",e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    @GetMapping("/api/task/findAllAppName")
    public Result findAppNameFromContacts(){
        try{
            Result result = new Result(RspCode.SUCCESS);
            List<ContactsGroup> contactsGroups = contactsGroupService.getAll();
            if (contactsGroups != null){
                List<String> list = contactsGroups.parallelStream().map(ContactsGroup::getGroupName).distinct().collect(Collectors.toList());
                result.setData(list);
            }
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("get findAppNameFromContacts error",e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 查询监控状态
     * @return
     */
    @GetMapping("/api/task/findMonitorStatus")
    public Result findMonitorStatus(){
        Result result = new Result(RspCode.SUCCESS);
        result.setData(BaseConstant.MONITOR_STATUS);
        return result;
    }

    /**
     * 启动单个任务
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/api/task/start/{id}")
    public Result startTask(@PathVariable Long id, @RequestParam(name = "offset") String offset, HttpServletRequest request) {
        try {
            //检测启动参数
            boolean m = offset.startsWith(BaseConstant.START_MODEL[2]);
            if (m){
                offset = offset.replace("time:","");
                try{
                    DateUtils.parse(offset,DateUtils.STYLE_FULL);
                }catch (Exception e){
                    e.printStackTrace();
                    return new Result(RspCode.PARAMS_ERROR,"启动时间格式错误！");
                }
            }
            boolean q = BaseConstant.START_MODEL[0].equals(offset) || BaseConstant.START_MODEL[1].equals(offset) || m;
            if (!q){
                return new Result(RspCode.EXCEPTION, "发布失败，消费位置错误！");
            }

            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);

            return dataTaskHandleService.startOne(id, offset, userId);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("start task error,id = "+id, e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 停止单个任务
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/api/task/stop/{id}")
    public Result stopTask(@PathVariable Long id, HttpServletRequest request){
        try {
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            return dataTaskHandleService.stopOne(id, userId);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("stop task error,id = "+id, e);
            return new Result(RspCode.UNKNOWN_ERROR);
        }
    }

    /**
     *批量启动任务
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/api/task/start")
    public Result batchStart(@RequestBody Map<String, Object> ids, HttpServletRequest request){
        try{
            List<Object> taskIds = (List<Object>) ids.get("taskIds");
            int success = 0;
            int fail = 0;
            for (Object id:taskIds) {
                try {
                    String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
                    //批量启动默认从最新位置消费
                    Result result = dataTaskHandleService.startOne(Long.valueOf(String.valueOf(id)),BaseConstant.START_MODEL[0],userId);
                    if (result.getCode().equals(RspCode.SUCCESS.code())){
                        success++;
                    }else {
                        fail++;
                    }
                }catch (Exception e){
                    logger.error(ExceptionUtils.getStackTrace(e));
                    Cat.logError("batch start task error, id = "+id, e);
                }
                Thread.sleep(3000);
            }
            Map<String,Integer> map = new HashMap(4);
            map.put("successCount",success);
            map.put("failCount",fail);

            Result result = new Result(RspCode.SUCCESS);
            result.setData(map);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("batch start task error!", e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 批量停止任务
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/api/task/stop")
    public Result batchStop(@RequestBody Map<String, Object> ids, HttpServletRequest request){
        try{
            List<Object> taskIds = (List<Object>) ids.get("taskIds");
            int success = 0;
            int fail = 0;
            for (Object id:taskIds) {
                try {
                    String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
                    Result result = dataTaskHandleService.stopOne(Long.valueOf(String.valueOf(id)),userId);
                    if (result.getCode().equals(RspCode.SUCCESS.code())){
                        success++;
                    }else {
                        fail++;
                    }
                }catch (Exception e){
                    logger.error(ExceptionUtils.getStackTrace(e));
                    Cat.logError("batch stop task error, id = "+id, e);
                }
            }
            Map<String,Integer> map = new HashMap(4);
            map.put("successCount",success);
            map.put("failCount",fail);

            Result result = new Result(RspCode.SUCCESS);
            result.setData(map);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("batch stop task error!", e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 批量删除
     * @param ids
     * @param request
     * @return
     */
    @PostMapping("/api/task/delete")
    public Result batchDelete(@RequestBody Map<String, Object> ids, HttpServletRequest request){
        try{
            List<Object> taskIds = (List<Object>) ids.get("taskIds");
            int success = 0;
            int fail = 0;
            for (Object id:taskIds) {
                try {
                    String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
                    Result result = dataTaskHandleService.deleteOne(Long.valueOf(String.valueOf(id)),userId);
                    if (result.getCode().equals(RspCode.SUCCESS.code())){
                        success++;
                    }else {
                        fail++;
                    }
                }catch (Exception e){
                    logger.error(ExceptionUtils.getStackTrace(e));
                    Cat.logError("batch delete task error, id = "+id, e);
                }
            }
            Map<String,Integer> map = new HashMap(4);
            map.put("successCount",success);
            map.put("failCount",fail);

            Result result = new Result(RspCode.SUCCESS);
            result.setData(map);
            return result;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("batch delete task error!", e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 查询任务状态：运行状态和监控状态 taskStatusAndMonitorStatus
     * @return
     */
    @PostMapping("/api/task/findStatus")
    public Result getStatus(@RequestBody Map<String, Object> ids, HttpServletRequest request){
        try{
            List<Object> taskIds = (List<Object>) ids.get("taskIds");
            List<Long> searchIds = taskIds.parallelStream().map(o -> Long.valueOf(String.valueOf(o))).collect(Collectors.toList());
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            return dataTaskHandleService.findStatus(userId, searchIds);
        }catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("findStatus error." , e);
            return new Result(RspCode.EXCEPTION);
        }
    }

    /**
     * 复制监控任务
     * @param copyVO
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/api/task/copy")
    public Result copyTask(@Valid @RequestBody DataTaskCopyVO copyVO, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()){
            logger.info("复制任务参数异常，{0}",copyVO);
            return new Result(RspCode.PARAMS_ERROR);
        }

        try {
            String userId = (String) request.getAttribute(BaseConstant.REQUEST_USER_ID_KEY);
            return dataTaskHandleService.copyTask(copyVO, userId);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            Cat.logError("copy error." , e);
            return new Result(RspCode.EXCEPTION);
        }
    }
}
