package com.rigai.rigeye.task.commit.service.impl;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.dianping.cat.Cat;
import com.rigai.rigeye.common.bean.AlertDataSetConstruct;
import com.rigai.rigeye.common.bean.AlertRuleConstruct;
import com.rigai.rigeye.common.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.common.constant.ComparisonOperator;
import com.rigai.rigeye.common.exception.DataException;
import com.rigai.rigeye.common.model.Contacts;
import com.rigai.rigeye.common.service.ContactsService;
import com.rigai.rigeye.task.commit.bean.AlertHistoryCache;
import com.rigai.rigeye.task.commit.exception.InfluxDBQueryException;
import com.rigai.rigeye.task.commit.influx.InfluxService;
import com.rigai.rigeye.task.commit.service.AlertChannelService;
import com.rigai.rigeye.task.commit.service.CommitAlertTaskService;
import com.em.fx.redis.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/12.
 *
 * 报警历史redis缓存结构
 *  key:HISTORY_CACHE_KEY (string 类型)
 *  vlue：类型： Map<Long,AlertHistoryCache>
 *  Map<Long,AlertHistoryCache> key为taskId
 */
@Component
@Scope("prototype")
public class CommitAlertTaskServiceImpl implements CommitAlertTaskService {


    /**
     * 用于规则判断2为只满足一个就预警
     */
    private static final int ONE_FOR_ALL=2;
    /**
     * 用于规则判断1为所有规则同时满足
     */
    private static final int ALL_FOR_ONE=1;

    @Autowired
    private InfluxService influxService;

    @Autowired
    private AlertChannelService alertChannelService;

    private static final int RUN_STATUS=1;

    private Long repeatLock;

    private static final String REPEAT_LOCK="repeatLock";

    private static final long SWITCH=1000;

    @Autowired
    RedisDao redisDao;

    @Autowired
    private ContactsService contactsService;

    private Logger logger= LoggerFactory.getLogger(CommitAlertTaskService.class);

    private static final String HISTORY_CACHE_KEY="aegis-task-history";

    @Override
    public void commitTask(AlertTaskInfo taskInfo) {
        logger.info("begin task {}",taskInfo);
        if(RUN_STATUS!=taskInfo.getTask().getStatus()){
            return;
        }
        int condition=taskInfo.getTask().getMuchRuleCondition();
        if(ONE_FOR_ALL==condition){
            oneHit(taskInfo);
        }
        if (ALL_FOR_ONE==condition){
            allHit(taskInfo);
        }
    }


    /**
     * 满足一条就报警的情况
     * @param taskInfo 报警任务
     */
    private void oneHit(AlertTaskInfo taskInfo){
        logger.info("analyze all for one alert task,task info: {}",taskInfo);
        AlertTaskDataConstruct dataConstruct=taskInfo.getTask();
        List<AlertDataSetConstruct> dataSetConstructs=dataConstruct.getAlertDataSet();
        //获取redis记录集合
        Map<Long,AlertHistoryCache> historyCache=(Map<Long,AlertHistoryCache>)redisDao.get(HISTORY_CACHE_KEY);
        Long taskId=dataConstruct.getId();
        AlertHistoryCache taskHistory=null;
        Date now=Calendar.getInstance().getTime();
        //如果总记录不为空则获取该任务的redis历史记录
        if(historyCache.size()>0){
            taskHistory=historyCache.get(taskId);
        }
        //如果改任务的redis历史不为空做防重复检查
        if(isRepeat(taskHistory)){
            return;
        }
        //获取报警联系人
        List<Contacts> contacts=getContacts(taskInfo);
        List<AlertRuleConstruct> ruleConstructs=taskInfo.getRules();
        //报警标示,每条规则都校验用于检验是否恢复告警，但告警只发第一次命中的规则
        //TODO 后续改进为规则合并
        boolean flag=true;
        //遍历规则
        for(AlertRuleConstruct rule:ruleConstructs){
            //获取规则对应的数据集
            AlertDataSetConstruct couple=getRuleCouple(rule,dataSetConstructs);
            //到influx查询
            double value;
            try{
                value=influxService.indexQuery(couple,rule);
            }catch (InfluxDBQueryException e){
                logger.error("influx db query error Data Set:{} | rule: {} | statement:{}",couple,rule,e.getMessage());
                Cat.logError("influx db query error",e);
                continue;
            }
            Long ruleId=rule.getRuleId();
            Long alertRuleId=null;
            //如果满足条件则报警  alert if
            logger.info("query value :{} and rule threshold is{} ",value,rule.getThreshold());
            //if(ComparisonOperator.compare(rule.getThreshold(),rule.getValueType(),value)&&flag){
            if(ComparisonOperator.compare(rule.getThreshold(),rule.getComparisonOperators(),value)&&flag){
                logger.info("all for one alert begin task id:{}",taskId);
                ArrayList<AlertRuleConstruct> alertRuleList=new ArrayList<>(1);
                alertRuleList.add(rule);
                alertChannelService.alert(taskInfo,alertRuleList,contacts);
                //获取该任务的记录
                //如果没有任务记录则创建
                if(taskHistory==null){
                    taskHistory=new AlertHistoryCache();
                }
                //更新记录
                taskHistory.addRecord(ruleId,now);
                historyCache.put(taskId,taskHistory);
                redisDao.setObj(HISTORY_CACHE_KEY,historyCache);
                alertRuleId=ruleId;
                flag=false;
            }//end alert if
            //如果还未报警或者报警的规则不是本条规则
            if(alertRuleId==null||!ruleId.equals(alertRuleId)){
                logger.info("begin to commit recover alert rule id is {},taskHistory is {}",alertRuleId,taskHistory);
                //获取该任务的记录
                //如果该任务及该规则有记录
                if(taskHistory!=null&&(taskHistory.getRuleIdDate(ruleId)!=null)){
                    //消除记录
                    taskHistory.remove(ruleId);
                    //恢复报警
                    alertChannelService.recoverAlert(taskInfo,ruleConstructs,contacts);
                }
            }
        }//end for
        //更新redis数据
        redisDao.setObj(HISTORY_CACHE_KEY,historyCache);
    }

    /**
     * 全满足才报警的情况
     * @param taskInfo 报警任务
     */
    private void allHit(AlertTaskInfo taskInfo){
        logger.info("analyze one for all alert task,task info: {}",taskInfo);
        Map<Long,AlertHistoryCache> historyCache=(Map<Long,AlertHistoryCache>)redisDao.get(HISTORY_CACHE_KEY);
        Date now=Calendar.getInstance().getTime();
        List<Contacts> contacts=getContacts(taskInfo);
        AlertTaskDataConstruct dataConstruct=taskInfo.getTask();
        Long taskId=dataConstruct.getId();
        //获取该任务的记录
        AlertHistoryCache taskHistory=historyCache.get(taskId);
        //如果有记录，检查重复发送条件
        if(isRepeat(taskHistory)){
            return;
        }
        List<AlertDataSetConstruct> dataSetConstructs=dataConstruct.getAlertDataSet();
        List<AlertRuleConstruct> ruleConstructs=taskInfo.getRules();
        int alertCount=0;
        for(AlertRuleConstruct rule:ruleConstructs){
            AlertDataSetConstruct couple=getRuleCouple(rule,dataSetConstructs);
            double value=influxService.indexQuery(couple,rule);
            if(ComparisonOperator.compare(rule.getThreshold(),rule.getComparisonOperators(),value)){
                alertCount++;
            }
        }
        //如果全部报警条件满足 alert if
        if(alertCount==ruleConstructs.size()){
            logger.info("one for all alert begin task id:{}",taskId);
            //执行报警
            alertChannelService.alert(taskInfo,contacts);
            //获取记录集合
            //获取该任务的记录
            taskHistory=historyCache.get(taskId);
            //如果没有记录则添加
            if(taskHistory==null){
                taskHistory=new AlertHistoryCache();
            }
            taskHistory.addRecord(-1L,now);
            historyCache.put(taskId,taskHistory);
            //更新redis数据
            redisDao.setObj(HISTORY_CACHE_KEY,historyCache);
            return;
        }//end alert if
        //不满足报警条件
        //如果有记录则消除
        if(taskHistory!=null){
            logger.debug("begin to commit recover alert");
            //直接消除任务级别
            historyCache.remove(taskId);
            redisDao.setObj(HISTORY_CACHE_KEY,historyCache);
            //发送恢复告警
            alertChannelService.recoverAlert(taskInfo,contacts);
        }
    }

    @PostConstruct
    public void initRedis(){
        if(!redisDao.hasKey(HISTORY_CACHE_KEY)){
            Map<Long,Map<Long,Date>> historyCache=new HashMap<>();
            redisDao.setObj(HISTORY_CACHE_KEY,historyCache);
        }
    }

    private AlertDataSetConstruct getRuleCouple(AlertRuleConstruct rule,List<AlertDataSetConstruct> dataSets){
        for(AlertDataSetConstruct dataSet:dataSets){
            if(rule.getVariable().equals(dataSet.getVariable())){
                return dataSet;
            }
        }
        throw new DataException("alert rule can't nest data set rule: "+rule);
    }

    private List<Contacts> getContacts(AlertTaskInfo taskInfo){
        //查询报警人员
        Contacts contactsParam=new Contacts();
        contactsParam.setGroupName(taskInfo.getTask().getAppName());
        return contactsService.getByObj(contactsParam);
    }

    @PostConstruct
    public void addListener(){
        Config config = ConfigService.getAppConfig();
        repeatLock=config.getLongProperty(REPEAT_LOCK,repeatLock)*SWITCH;
        config.addChangeListener(changeEvent -> changeEvent.changedKeys().stream().filter(REPEAT_LOCK::equals).forEach(key -> {
            ConfigChange change = changeEvent.getChange(key);
            repeatLock = Long.valueOf(change.getNewValue())*SWITCH;
        }));
    }


    /**
     * 是否为重复报警
     * @param taskHistory 报警历史
     * @return
     */
    private boolean isRepeat(AlertHistoryCache taskHistory){
        if(taskHistory!=null){
            Date now=Calendar.getInstance().getTime();
            Date lastTime=taskHistory.getLastAlertDate();
            long between=now.getTime()-lastTime.getTime();
            if(between<repeatLock){
                return true;
            }
        }
        return false;
    }
}
