package com.rigai.rigeye.task.get.service.impl;

import com.dianping.cat.Cat;
import com.rigai.rigeye.common.bean.*;
import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.common.exception.DataException;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.rigai.rigeye.common.model.AlertRule;
import com.rigai.rigeye.common.model.AlertTask;
import com.rigai.rigeye.common.service.AlertHistoryDetailService;
import com.rigai.rigeye.common.service.AlertRuleService;
import com.rigai.rigeye.common.service.AlertTaskService;
import com.rigai.rigeye.task.get.service.AlertApiService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * 获取预计信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AlertTaskInfo> getAllAlertTask(){
        List<AlertTask> data =alertTaskService.getAll();
        if(data==null||data.size()<1){
            return null;
        }
        //初始化回传结果
        ArrayList<AlertTaskInfo> resultsData=new ArrayList<>(data.size());
        //遍历报警任务
        data.parallelStream().forEach(alertTask->{
            //生成回传结果中的预警任务部分
            AlertTaskDataConstruct construct=new AlertTaskDataConstruct(alertTask);
            //根据预警任务ID查找预警规则
            AlertRule ruleParam=new AlertRule();
            ruleParam.setAlertTaskId(alertTask.getId());
            //预警规则列表
            List<AlertRule> rules=alertRuleService.getByObj(ruleParam);
            AlertTaskInfo info=new AlertTaskInfo(construct,toAlertRuleConstructList(rules));
            resultsData.add(info);
        });
        return resultsData;
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
    public List<AlertRule> toAlertRule(List<AlertRuleConstruct> ruleConstructs, Long taskId, String user){
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
