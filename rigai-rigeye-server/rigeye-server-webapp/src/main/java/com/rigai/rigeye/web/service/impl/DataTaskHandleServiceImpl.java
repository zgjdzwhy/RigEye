package com.rigai.rigeye.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.model.DataTask;
import com.rigai.rigeye.common.service.DataSetRuleService;
import com.rigai.rigeye.common.service.DataSourceService;
import com.rigai.rigeye.common.service.DataTaskService;
import com.rigai.rigeye.data.service.SparkEngine;
import com.rigai.rigeye.web.bean.vo.DataTaskCopyVO;
import com.rigai.rigeye.web.bean.vo.DataTaskRedisVO;
import com.rigai.rigeye.web.bean.vo.DataTaskStatusVO;
import com.rigai.rigeye.web.constant.BaseConstant;
import com.rigai.rigeye.web.service.DataTaskHandleService;
import com.rigai.rigeye.web.util.InfluxDBUtil;
import com.em.fx.common.bean.Result;
import com.em.fx.common.bean.constant.RspCode;
import com.em.fx.redis.dao.RedisDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yh
 * @date 2018/8/22 16:50
 */
@Service
public class DataTaskHandleServiceImpl implements DataTaskHandleService {
    private Logger logger= LoggerFactory.getLogger(DataTaskHandleServiceImpl.class);
    @Autowired
    RedisDao redisDao;
    @Autowired
    DataTaskService dataTaskService;
    @Autowired
    SparkEngine sparkEngine;
    @Autowired
    DataSetRuleService dataSetRuleService;
    @Autowired
    DataSourceService dataSourceService;

    @Override
    public Result startOne(Long id, String offset, String userId) throws Exception {

        DataSetRule rule = new DataSetRule();
       // rule.setUserId(userId);启动任务不需要对用户id鉴权
        rule.setTaskId(id);
        List<DataSetRule> dataSetRules = dataSetRuleService.findByObj(rule);
        //校验任务是否有数据集
        if (dataSetRules == null || dataSetRules.size() < 1){
            return new Result(RspCode.EXCEPTION, "发布失败，任务没有数据集！");
        }
        //校验任务是否存在
        DataTask dataTask = dataTaskService.getById(id);
        if (dataTask == null){
            return new Result(RspCode.EXCEPTION, "发布失败，任务不存在！");
        }
        //设置启动位置
        dataTask.setOffset(offset);
        //判断是否能启动，运行中和等待中状态的任务不能再次启动。
        boolean f= (BaseConstant.WAITING == dataTask.getTaskStatus()) || (BaseConstant.RUNNING == dataTask.getTaskStatus());
        if (f){
            return new Result(RspCode.EXCEPTION, "发布失败，等待中和运行中的任务无法再次启动！");
        }
        //发布任务
        //获取spark参数，提交spark任务
        String param = dataTask.getStartParam();
        Result result;
        Map<String, String> map = new HashMap<>(8);
        //设置应用名称
        map.put("spark.app.name",dataTask.getTaskName());
        if (StringUtils.isNotEmpty(param)) {
            //其他启动参数
            try{
                Map<String, String> p = (Map<String, String>) JSON.parse(param);
                map.putAll(p);
            }catch (Exception e){
                logger.error("发布失败，任务的启动参数不合规范！",e);
                //启动参数有错误，启动失败
                dataTask.setTaskStatus(BaseConstant.FAILED);
                dataTaskService.update(dataTask);
                return new Result(RspCode.EXCEPTION, "发布失败，任务的启动参数不合规范！");
            }
        }
        String [] args = {
                id.toString(),
        };
        //提交任务
        result = sparkEngine.submitAppTask(map,args);

        //任务提交成功
        if (result.getCode().equals(RspCode.SUCCESS.code())) {
            try{
                //1、下发配置到redis
                DataTaskRedisVO dataTaskRedisVO = new DataTaskRedisVO();
                //设置基本参数
                BeanUtils.copyProperties(dataTask, dataTaskRedisVO);
                //查询数据源
                DataSourceDTO dataSource = dataSourceService.getDataSourceDTOById(Long.valueOf(dataTask.getDataSourceId()));
                dataTaskRedisVO.setDataSourceDTO(dataSource);
                if (dataTask.getDataSinkId() != -1) {
                    //明细数据输出数据源
                    DataSourceDTO dataSink = dataSourceService.getDataSourceDTOById(Long.valueOf(dataTask.getDataSinkId()));
                    dataTaskRedisVO.setDataSink(dataSink);
                }
                //设置数据集
                dataTaskRedisVO.setDataSetRule(dataSetRules);

                //写入redis
                boolean t = redisDao.setStr(BaseConstant.PREFIX_DATAT_ASK + dataTask.getId(), JSON.toJSONString(dataTaskRedisVO));
                if (t){
                    //下发配置正确。保存任务提交的id，更改任务状态为等待中。
                    dataTask.setAppRunId(result.getData().toString());
                    dataTask.setTaskStatus(BaseConstant.WAITING);
                    dataTaskService.update(dataTask);
                    //返回成功
                    logger.info(MessageFormat.format("start task success , [time:{0},userId : {1}, taskId :{2} ]", System.currentTimeMillis(), userId, id));
                    return new Result(RspCode.SUCCESS);
                }else {
                    //配置信息下发失败。取消任务,任务状态改为发布失败。
                    sparkEngine.killAppTask(result.getData().toString());
                    dataTask.setTaskStatus(BaseConstant.FAILED);
                    dataTaskService.update(dataTask);
                    logger.info(MessageFormat.format("start task failed ,write to redis failed, [time:{0},userId : {1}, taskId :{2} ]", System.currentTimeMillis(), userId, id));
                    return new Result(RspCode.EXCEPTION, "发布失败，下发配置信息失败！");
                }
            }catch (Exception e){
                logger.error("任务提交成功,后续步骤出错，撤销任务:",e);
                sparkEngine.killAppTask(result.getData().toString());
                throw new Exception("下发配置出错,撤销任务");
            }
        }else {
            //任务提交失败，更新任务状态为失败
            dataTask.setTaskStatus(BaseConstant.FAILED);
            dataTaskService.update(dataTask);
            logger.info(MessageFormat.format("start task failed , message:{0}, [time:{1},userId : {2}, taskId :{3} ]", result.getMessage(), System.currentTimeMillis(), userId, id));
            return result;
        }

    }

    @Override
    public Result stopOne(Long id, String userId) {
        DataTask dataTask = new DataTask();
        dataTask.setId(id);
        dataTask.setUserId(userId);
        List<DataTask> dataTasks = dataTaskService.getByObj(dataTask);
        if (dataTasks == null || dataTasks.size() < 1) {
            return new Result(RspCode.EXCEPTION,"停止失败，任务不存在！");
        }

        dataTask = dataTasks.get(0);
        //运行中和等待中的任务才能停止
        boolean f = dataTask.getTaskStatus().equals(BaseConstant.RUNNING) || dataTask.getTaskStatus().equals(BaseConstant.WAITING);
        if (!f){
            return new Result(RspCode.EXCEPTION,"停止失败，任务已处于停止状态！");
        }
        //发送停止任务的命令
        Result result = sparkEngine.killAppTask(dataTask.getAppRunId());
        //停止成功
        if (result.getCode().equals(RspCode.SUCCESS.code())) {
            //删除下发的配置信息
            redisDao.del(BaseConstant.PREFIX_DATAT_ASK + dataTask.getId());
            //更新数据库状态为停止
            dataTask.setTaskStatus(BaseConstant.STOPPED);
            dataTaskService.update(dataTask);
            logger.info(MessageFormat.format("stop task success : [time:{0},userId : {1}, taskId :{2} ]", System.currentTimeMillis(), userId, id));
            return new Result(RspCode.SUCCESS);
        }else {
            //停止失败
            return result;
        }
    }

    @Override
    public Result deleteOne(Long id, String userId) {
        DataTask dataTask = new DataTask();
        dataTask.setUserId(userId);
        dataTask.setId(id);

        List<DataTask> dataTasks = dataTaskService.getByObj(dataTask);
        if (dataTasks == null || dataTasks.size() < 1){
            return new Result(RspCode.EXCEPTION,"删除失败, 监控任务不存在！");
        }

        dataTask = dataTasks.get(0);
        //检查状态是否允许删除,运行中和等待运行状态的任务不能删除。任务状态（默认 1=created:新建；2=waiting：等待中；3=running:运行中；4=failed：运行失败；5=stopped:停止)
        boolean f = dataTask.getTaskStatus().equals(BaseConstant.WAITING) || dataTask.getTaskStatus().equals(BaseConstant.RUNNING);
        if (f){
            return new Result(RspCode.EXCEPTION, "删除失败，运行中或等待中的任务不能删除！");
        }

        //删除redis配置、删除数据库配置。
        redisDao.del(BaseConstant.PREFIX_DATAT_ASK+id);
        //删除任务绑定的数据集列表
        DataSetRule rule = new DataSetRule();
        rule.setTaskId(id);
        rule.setUserId(userId);
        dataSetRuleService.delete(rule);
        //删除监控任务
        dataTaskService.delete(dataTask);
        return new Result(RspCode.SUCCESS);
    }

    @Override
    public Result findStatus(String userId,List<Long> ids) {
        //查询所有监控任务
        List<DataTask> dataTasks = dataTaskService.findDataTaskByUserIdAndIdIn(userId, ids);
        if (dataTasks == null || dataTasks.size() < 1){
            return new Result(RspCode.SUCCESS);
        }

        List<DataTaskStatusVO> dataTaskStatusVOS = dataTasks.parallelStream().map(task -> {
            DataTaskStatusVO dataTaskStatusVO = new DataTaskStatusVO(task.getId(),task.getTaskStatus(), task.getMonitorStatus());
            //查询一小时内influx db中是否有切分异常的数据。有切分异常数据则监控状态(monitorStatus)改为异常状态.
            boolean divide = InfluxDBUtil.countBadDivide(task.getId(), "bad");
            dataTaskStatusVO.setMonitorStatus(divide ? 1 : 2);
            dataTaskStatusVO.setMonitorMessage(divide ? "最近一小时内 :健康" : "最近一小时内 :出现过异常");
            //运行中和等待中的任务需要重新查询状态，并更新状态。其他状态的任务不需要重新查询和更新。
            boolean f = task.getTaskStatus().equals(BaseConstant.WAITING) || task.getTaskStatus().equals(BaseConstant.RUNNING);
            if (f){
                Result result = sparkEngine.getAppTaskStatus(task.getAppRunId());
                Object data = result.getData();
                if (result.getCode().equals(RspCode.SUCCESS.code()) && Integer.parseInt(data.toString()) > 0){
                    //更新数据库任务状态
                    task.setTaskStatus(Integer.parseInt(data.toString()));
                    dataTaskStatusVO.setTaskStatus(Integer.parseInt(data.toString()));
                    dataTaskService.update(task);
                }
            }
            return dataTaskStatusVO;
        }).collect(Collectors.toList());

        Result result = new Result(RspCode.SUCCESS);
        result.setData(dataTaskStatusVOS);
        return result;
    }

    @Transactional
    @Override
    public Result copyTask(DataTaskCopyVO copyVO, String userId) {
        DataTask dataTask = new DataTask();
        dataTask.setUserId(userId);
        dataTask.setId(copyVO.getId());
        List<DataTask> dataTasks = dataTaskService.getByObj(dataTask);
        //任务是否存在
        if (dataTasks == null || dataTasks.size() < 1){
            return new Result(RspCode.NO_RESULT,"监控任务不存在！");
        }

        //名字是否已经存在
        List<String> taskNames = dataTaskService.findDataTaskNameByUserId(userId);
        for (String s : taskNames){
            if (copyVO.getName().equals(s)) {
                return new Result(RspCode.EXCEPTION, "任务名称已存在！");
            }
        }

        dataTask = dataTasks.get(0);
        dataTask.setId(0L);
        dataTask.setTaskName(copyVO.getName());
        dataTask.setAppName(copyVO.getAppName());
        //任务状态改为新建
        dataTask.setTaskStatus(BaseConstant.CREATE);
        //监控状态改为默认：正常
        dataTask.setMonitorStatus(BaseConstant.MONITOR_STATUS[1]);
        //提交任务生成的id设置为空
        dataTask.setAppRunId(BaseConstant.BLANK);
        dataTask.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

        //修改id后插入新任务
        dataTaskService.insert(dataTask);
        //新任务的id
        Long id = dataTask.getId();
        //复制数据集
        DataSetRule rule = new DataSetRule();
        rule.setUserId(userId);
        //id需要是被复制任务的id
        rule.setTaskId(copyVO.getId());
        List<DataSetRule> dataSetRules = dataSetRuleService.getByObj(rule);
        if (dataSetRules != null && dataSetRules.size() > 0){
            dataSetRules = dataSetRules.parallelStream().peek(dataSetRule -> {
                //更新成新任务id
                dataSetRule.setTaskId(id);
                dataSetRule.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            }).collect(Collectors.toList());
            //插入新任务
            dataSetRules.forEach(dataSetRule -> dataSetRuleService.save(dataSetRule));
        }
        return new Result(RspCode.SUCCESS);
    }
}
