package com.rigai.rigeye.alert.service.impl;

import com.dianping.cat.Cat;
import com.rigai.rigeye.alert.bean.*;
import com.rigai.rigeye.alert.service.AlertApiService;
import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.config.AuthorizationConfig;
import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.common.exception.DataException;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.rigai.rigeye.common.model.AlertRule;
import com.rigai.rigeye.common.model.AlertTask;
import com.rigai.rigeye.common.service.AlertHistoryDetailService;
import com.rigai.rigeye.common.service.AlertRuleService;
import com.rigai.rigeye.common.service.AlertTaskService;
import com.rigai.rigeye.common.service.ModuleApiService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rigai.rigeye.alert.bean.AlertHistoryDetailInfo;
import com.rigai.rigeye.alert.bean.AlertRuleConstruct;
import com.rigai.rigeye.alert.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.alert.bean.AlertTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/21.
 */
@Service
public class AlertApiServiceImpl implements AlertApiService {


    private Logger logger= LoggerFactory.getLogger(AlertApiServiceImpl.class);

    @Autowired
    AlertRuleService alertRuleService;

    @Autowired
    AlertTaskService alertTaskService;

    @Autowired
    AlertHistoryDetailService alertHistoryDetailService;

    @Resource
    ModuleApiService moduleApiService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addAlertTask(AlertTaskDataConstruct alertDataConstruct, List<AlertRuleConstruct> ruleConstructs, String user) {
        //构造预警任务
        AlertTask alertTask=alertDataConstruct.toAlertTask();
        alertTask.setUserId(user);
        //新建任务默认为新建状态,正常状态
        alertTask.setStatus(0);
        alertTask.setMonitorStatus(0);
        alertTask.setCreateTime(Calendar.getInstance().getTime());
        alertTaskService.insert(alertTask);
        Long taskId=alertTask.getId();
        if(taskId==null){
            throw new RuntimeException("alert task not return Id");
        }
        //构造预警规则
        List<AlertRule> rules=toAlertRule(ruleConstructs,taskId,user);
        alertRuleService.insert(rules);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editAlertTask(AlertTaskDataConstruct alertDataConstruct, List<AlertRuleConstruct> ruleConstructs, String user) {
        AlertTask alertParam=new AlertTask();
        alertParam.setUserId(user);
        alertParam.setId(alertDataConstruct.getId());
        List<AlertTask> has=alertTaskService.getByObj(alertParam);
        if(has==null||has.size()<1){
            return false;
        }
        AlertTask alertTask=alertDataConstruct.toAlertTask();
        alertTask.setUserId(user);
        alertTask.setUpdateTime(Calendar.getInstance().getTime());
        alertTaskService.update(alertTask);
        Long taskId=alertTask.getId();
        if(taskId==null){
            throw new RuntimeException("alert task no return Id");
        }
        List<AlertRule> rules=toAlertRule(ruleConstructs,taskId,user);
        AlertRule oldRuleParam=new AlertRule();
        oldRuleParam.setAlertTaskId(taskId);
        List<AlertRule> oldRules=alertRuleService.getByObj(oldRuleParam);
        //删除旧预警规则
        alertRuleService.delete(oldRules);
        //插入新预警规则
        alertRuleService.insert(rules);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAlertTask(Long taskId,String user){
        //删除预警任务
        AlertTask taskParam=new AlertTask();
        taskParam.setUserId(user);
        taskParam.setId(taskId);
        int result=alertTaskService.delete(taskParam);

        AlertRule ruleParam=new AlertRule();
        //删除对应的预警规则
        ruleParam.setAlertTaskId(taskId);
        ruleParam.setUserId(user);
        List<AlertRule> oldRules=alertRuleService.getByObj(ruleParam);
        alertRuleService.delete(oldRules);
        return true;
    }

    @Override
    public AlertTaskInfo getAlertTask(Long taskId, String userId) {
        AlertTask param=new AlertTask();
        param.setId(taskId);
        AlertTask task=alertTaskService.getById(taskId);
        if(task==null||(!task.getUserId().equals(userId))){
            return null;
        }
        AlertTaskDataConstruct construct=new AlertTaskDataConstruct(task);
        AlertRule ruleParam=new AlertRule();
        ruleParam.setAlertTaskId(taskId);
        ruleParam.setUserId(userId);
        //预警规则列表
        List<AlertRule> rules=alertRuleService.getByObj(ruleParam);
        List<AlertRuleConstruct> ruleConstructs=null;
        if(rules!=null){
            ruleConstructs=toAlertRuleConstructList(rules);
        }
        return new AlertTaskInfo(construct,ruleConstructs);
    }

    @Override
    public List<AlertTask> getAll() {
        return alertTaskService.getAll();
    }

    @Override
    public PageInfo<AlertHistoryDetailInfo> pageGetAlertHistoryDetailInfo(AlertHistoryQueryParam param, Integer page, Integer pageSize) {
        //分页查找数据
        PageInfo<AlertHistoryDetail> detailPageInfo=alertHistoryDetailService.pageQueryByAlertHistoryDetailInfo(param,page,pageSize);
        //如果为空或结果为0
        if(detailPageInfo==null){
            return null;
        }
        List<AlertHistoryDetail> result=detailPageInfo.getList();
        if(result==null||result.size()<1){
            return null;
        }
        //实际要返回的数据数组
        ArrayList<AlertHistoryDetailInfo> infos=new ArrayList<>(result.size());
        //遍历报警记录
        result.parallelStream().forEach(detail->{
            //将除报警规则id外的字段拷贝到返回对象
            AlertHistoryDetailInfo info=new AlertHistoryDetailInfo();
            BeanUtils.copyProperties(detail,info);
            //报警规则id获取
            String[] ruleIdStr=detail.getAlertRuleIds().split(CommonConstant.SPLIT_SIGN);
            StringBuilder alertRuleStr=new StringBuilder();
            //遍历报警规则id
            for(String ruleId:ruleIdStr){
                //获取报警规则
                Long id=Long.valueOf(ruleId);
                AlertRule rule=alertRuleService.getById(id);
                if(rule==null){
                    Cat.logError("Alert history detail id:"+detail.getId()+"wrong data "+"no data with rule id "+ruleId,new DataException("Alert history detail id:"+detail.getId()+"wrong data"));
                    logger.error("Alert history detail id:"+detail.getId()+"wrong data "+"no data with rule id "+ruleId);
                    continue;
                }
                //转换为报警规则结构实体，通过重写后的toString生成信息，保持和其他页面一致
                AlertRuleConstruct ruleConstruct=new AlertRuleConstruct(rule);
                alertRuleStr.append(ruleConstruct.toString());
                alertRuleStr.append(System.lineSeparator());
            }
            if(alertRuleStr.length()>0){
                info.setAlertRule(alertRuleStr.toString());
            }
            //完成返回对象构造
            infos.add(info);
        });
        //重构分页信息
        PageInfo<AlertHistoryDetailInfo> alertRuleConstructPageInfo=new PageInfo<>(infos);
        alertRuleConstructPageInfo.setTotal(detailPageInfo.getTotal());
        alertRuleConstructPageInfo.setPages(detailPageInfo.getPages());
        return alertRuleConstructPageInfo;
    }

    @Override
    public void stopTask(Long taskId) {
        AlertTask task=alertTaskService.getById(taskId);
        if(task!=null){
            task.setStatus(2);
            alertTaskService.update(task);
        }
    }

    @Override
    public void startTask(Long taskId) {
        AlertTask task=alertTaskService.getById(taskId);
        if(task!=null){
            task.setStatus(1);
            alertTaskService.update(task);
        }
    }


    /**
     * 获取预计信息列表
     * @param page 页面
     * @param pageSize 每页条数
     * @param param 查询参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<AlertTaskInfo> pageGetAlertTask(int page,int pageSize,AlertTask param,String trueUser){
        String user=param.getUserId();
        //根据参数获取报警任务
        //如果真实用户不为空且权限功能开启
        Set<String> modules=null;
        if(trueUser!=null&& AuthorizationConfig.isAuthorizationEnable()){
            if(!AuthorizationConfig.isAdmin(trueUser)){
                modules=moduleApiService.getUserModule(trueUser);
                logger.info("user {} access module: {}",trueUser,modules);
                if(modules==null||modules.size()<1){
                    return null;
                }
            }
        }
        logger.info("user {} access module: {} .",trueUser,modules);
        PageHelper.orderBy("update_time desc");
        PageHelper.startPage(page,pageSize);
        List<AlertTask> data=alertTaskService.listAlertTask(param,modules);
        PageInfo<AlertTask> alertTaskPageInfo=new PageInfo<>(data);
        if(data==null||data.size()<1){
            return null;
        }
        //初始化回传结果
        ArrayList<AlertTaskInfo> resultsData=new ArrayList<>(data.size());
        //遍历报警任务
        data.parallelStream().forEachOrdered(alertTask->{
            //生成回传结果中的预警任务部分
            AlertTaskDataConstruct construct=new AlertTaskDataConstruct(alertTask);
            //根据预警任务ID查找预警规则
            AlertRule ruleParam=new AlertRule();
            ruleParam.setAlertTaskId(alertTask.getId());
            ruleParam.setUserId(user);
            //预警规则列表
            List<AlertRule> rules=alertRuleService.getByObj(ruleParam);
            AlertTaskInfo info=new AlertTaskInfo(construct,toAlertRuleConstructList(rules));
            resultsData.add(info);
        });
        //最终回传
        PageInfo<AlertTaskInfo> result=new PageInfo<>();
        //总数据条数
        result.setTotal(alertTaskPageInfo.getTotal());
        //总页数
        result.setPages(alertTaskPageInfo.getPages());
        //结果数据
        result.setList(resultsData);
        return result;
    }

    /**
     * 用于类型转换
     * @param rules
     * @return
     */
    @Override
    public List<AlertRuleConstruct> toAlertRuleConstructList(List<AlertRule> rules){
        if(rules==null||rules.size()<1){
            return null;
        }
        ArrayList<AlertRuleConstruct> result=new ArrayList<>(rules.size());
        rules.parallelStream().forEach(rule->{
            AlertRuleConstruct ruleConstruct=new AlertRuleConstruct(rule);
            result.add(ruleConstruct);
        });
        return result;
    }

    /**
     * 用于类型转换
     * @param ruleConstructs
     * @param user
     * @return
     */
    @Override
    public List<AlertRule> toAlertRule(List<AlertRuleConstruct> ruleConstructs, Long taskId,String user){
        ArrayList<AlertRule> rules=new ArrayList<>(ruleConstructs.size());
        ruleConstructs.parallelStream().forEach(rule->{
            AlertRule temp=rule.toAlertRule();
            temp.setUserId(user);
            temp.setAlertTaskId(taskId);
            rules.add(temp);
        });
        return rules;
    }

}
