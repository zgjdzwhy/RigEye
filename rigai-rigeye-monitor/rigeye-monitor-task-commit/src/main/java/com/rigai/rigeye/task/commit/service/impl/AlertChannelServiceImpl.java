package com.rigai.rigeye.task.commit.service.impl;

import com.dianping.cat.Cat;
import com.rigai.rigeye.common.bean.AlertRuleConstruct;
import com.rigai.rigeye.common.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.common.constant.AlertLevelDescription;
import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.common.model.AlertFaultProcessor;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.rigai.rigeye.common.model.Contacts;
import com.rigai.rigeye.common.service.AlertFaultProcessorService;
import com.rigai.rigeye.common.service.AlertHistoryDetailService;
import com.rigai.rigeye.task.commit.bean.notice.mail.MailInfo;
import com.rigai.rigeye.task.commit.bean.notice.sms.ShotMessageInfo;
import com.rigai.rigeye.task.commit.bean.notice.weixin.WeiXinReqInfo;
import com.rigai.rigeye.task.commit.notice.NoticeUtil;
import com.rigai.rigeye.task.commit.service.AlertChannelService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/13.
 */

@Component
public class AlertChannelServiceImpl implements AlertChannelService {

    @Resource(name = "noticeUtil")
    private NoticeUtil noticeUtil;

    @Autowired
    private AlertHistoryDetailService alertHistoryDetailService;

    @Autowired
    private AlertFaultProcessorService alertFaultProcessorService;

    private Logger logger = LoggerFactory.getLogger(AlertChannelService.class);

    private static final String ALERT_TITLE="";

    private static final String ALERT_MAIL_TITLE="日志监控系统";

    private static final String RECOVER_TITLE="恢复告警";

    private static final String NOTICE_PROJECT="日志监控平台";

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");

    @Override
    public void alert(AlertTaskInfo taskInfo, List<AlertRuleConstruct> rules,List<Contacts> contacts) {
        commonAlert(taskInfo,rules,contacts,ALERT_TITLE);
    }

    private MailInfo packageMailInfo(List<Contacts> contacts, String content,String title) {
        StringBuilder mails=new StringBuilder();
        for(Contacts contact:contacts){
            mails.append(contact.getMail());
            mails.append(CommonConstant.SPLIT_SIGN);
        }
        MailInfo mailInfo=new MailInfo();
        mailInfo.setTitle(title);
        mailInfo.setContent(content);
        mailInfo.setSubject(title);
        if (StringUtils.isBlank(title)){
            mailInfo.setSubject(ALERT_MAIL_TITLE);
        }
        mailInfo.setReceiver(mails.substring(0,mails.length()-1));
        return mailInfo;
    }

    private WeiXinReqInfo packageWeiXinParam(List<Contacts> contacts, String content) {
        StringBuilder mails=new StringBuilder();
        for(Contacts contact:contacts){
            mails.append(contact.getMail());
            mails.append(CommonConstant.SPLIT_SIGN);
        }
        return new WeiXinReqInfo(mails.substring(0,mails.length()-1),content);
    }

    /**
     * 根据报警内容和联系人组织发送短信所需参数
     * @param contacts 联系人
     * @param content 报警内容
     * @return 报警短信参数
     */
    private ShotMessageInfo packageSMSParam(List<Contacts> contacts, String content) {
        ShotMessageInfo shotMessageInfo=new ShotMessageInfo();
        shotMessageInfo.setContent(content);
        StringBuilder numbers=new StringBuilder();
        for(Contacts contact:contacts){
            numbers.append(contact.getPhone());
            numbers.append(CommonConstant.SPLIT_SIGN);
        }
        shotMessageInfo.setPhoneNumbers(numbers.substring(0,numbers.length()-1));
        return shotMessageInfo;
    }

    /**
     * 组织报警内容
     * @param taskInfo 报警任务信息
     * @param rules 命中的报警规则信息
     * @return 报警内容
     */
    private String buildAlertContent(AlertTaskInfo taskInfo, List<AlertRuleConstruct> rules,String title) {
        System.out.println("=========================================================");
        System.out.println("title:"+title);
        System.out.println("=========================================================");
        StringBuilder alertContent=new StringBuilder(title);
        System.out.println(alertContent.toString());
        System.out.println("=========================================================");
        alertContent.append("[");
        alertContent.append(sdf.format(Calendar.getInstance().getTime()));
        alertContent.append("]");
        alertContent.append("[");
        alertContent.append(NOTICE_PROJECT);
        alertContent.append("]");
        alertContent.append("[");
        alertContent.append(taskInfo.getTask().getAppName());
        alertContent.append("]");
        alertContent.append("[");
        for (int i=0;i<rules.size();i++){
            alertContent.append(rules.get(i).toString());
            if(i!=rules.size()-1){
                alertContent.append(CommonConstant.SPLIT_SIGN);
            }
        }
        alertContent.append("]");
        alertContent.append("[");
        alertContent.append(AlertLevelDescription.getDescription(taskInfo.getTask().getLevel()));
        alertContent.append("]");
        return alertContent.toString();
    }

    /**
     * 通用报警，负责将报警写入数据库，如果在通知时间内则通知联系人
     * @param taskInfo 报警任务
     * @param rules 报警规则
     * @param title 标题
     */
    private void commonAlert(AlertTaskInfo taskInfo, List<AlertRuleConstruct> rules,List<Contacts> contacts,String title){
        if(rules==null){
            rules=taskInfo.getRules();
        }
        //获取报警渠道，全满足才报警取所有规则的渠道合集，只满足一条规则就报警则显然只会取到单条规则的报警渠道
        Set<Integer> channel=new HashSet<>();
        StringBuilder builder=new StringBuilder();
        rules.parallelStream().forEach(rule-> {
            builder.append(rule.getRuleId());
            builder.append(",");
            rule.getChannel().parallelStream().forEach(ruleChannel-> channel.add(Integer.valueOf(ruleChannel)));});
        String ruleIds=builder.toString().substring(0,builder.length()-1);
        logger.info("begin to commit alert taskId: {} | alert ruleIds:{}",taskInfo.getTask().getId(),ruleIds);
        //组织报警内容
        String content=buildAlertContent(taskInfo,rules,title);
        //更新故障表
        Integer alertFaultId=updateAlertFault(taskInfo,rules,ruleIds);
        //添加报警历史
        recordHistory(taskInfo,ruleIds,content,alertFaultId,contacts);
        //根据报警渠道通知联系人
        if(inAlertTime(taskInfo.getTask())){
            noticeContacts(channel,contacts,content,title);
        }
    }

    /**
     * 判断当前是否在报警任务通知时间
     * @param task 报警任务
     * @return 如果在通知时间则告警
     */
    public boolean inAlertTime(AlertTaskDataConstruct task) {
        Calendar calendar=Calendar.getInstance();
        Long now=calendar.getTime().getTime();
        String[] beginTimeStr=task.getNoticeStartTime().split(":");
        String[] endTimeStr=task.getNoticeEndTime().split(":");
        calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(beginTimeStr[0]));
        calendar.set(Calendar.MINUTE,Integer.valueOf(beginTimeStr[1]));
        Long beginTime=calendar.getTime().getTime();
        calendar.set(Calendar.HOUR_OF_DAY,Integer.valueOf(endTimeStr[0]));
        calendar.set(Calendar.MINUTE,Integer.valueOf(endTimeStr[1]));
        Long endTime=calendar.getTime().getTime();
        return now >= beginTime && now <= endTime;
    }


    /**
     * 根据渠道，联系人和内容发送告警
     * @param channel 渠道列表
     * @param contacts 联系人列表
     * @param content 内容
     */
    private void noticeContacts(Set<Integer> channel, List<Contacts> contacts, String content, String title){
        channel.parallelStream().forEach(commitChannel->{
            switch (commitChannel){
                case 1:
                    ShotMessageInfo shotMessageInfo=packageSMSParam(contacts,content);
                    try {
                        noticeUtil.sendMessage(shotMessageInfo);
                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                        Cat.logError("发送短信错误 参数："+shotMessageInfo,e);
                    }
                    break;
                case 2:
                    WeiXinReqInfo weiXinReqInfo=packageWeiXinParam(contacts,content);
                    try {
                        noticeUtil.sendWeiXin(weiXinReqInfo);
                    } catch (Exception e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                        Cat.logError("发送微信错误 参数："+weiXinReqInfo,e);
                    }
                    break;
                case 3:
                    MailInfo mailInfo=packageMailInfo(contacts,content,title);
                    try {
                        noticeUtil.sendMail(mailInfo);
                    } catch (IOException e) {
                        logger.error(ExceptionUtils.getStackTrace(e));
                        Cat.logError("发送邮件错误 参数："+mailInfo,e);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void alert(AlertTaskInfo taskInfo,List<Contacts> contacts) {
        alert(taskInfo,null,contacts);
    }

    @Override
    public void recoverAlert(AlertTaskInfo taskInfo,List<Contacts> contacts) {
        recoverAlert(taskInfo,null,contacts);
    }

    @Override
    public void recoverAlert(AlertTaskInfo taskInfo, List<AlertRuleConstruct> rules,List<Contacts> contacts) {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("恢复告警");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        commonAlert(taskInfo,rules, contacts,RECOVER_TITLE);
    }

    /**
     * 更新报警历史详细
     * @param taskInfo 任务信息
     * @param ruleIds 命中规则
     * @param content 报警内容
     * @param alertFaultId 对应故障表数据id
     */
    private void recordHistory(AlertTaskInfo taskInfo,String ruleIds,String content,Integer alertFaultId,List<Contacts> contactses){
        AlertHistoryDetail historyDetail=new AlertHistoryDetail();
        historyDetail.setAlertContent(content);
        historyDetail.setAlertName(taskInfo.getTask().getName());
        historyDetail.setAlertRuleIds(ruleIds);
        historyDetail.setAppName(taskInfo.getTask().getAppName());
        historyDetail.setAlertTime(Calendar.getInstance().getTime());
        historyDetail.setAlertType(taskInfo.getTask().getLevel());
        historyDetail.setAlertFaultId(alertFaultId);
        long taskId=taskInfo.getTask().getId();
        historyDetail.setTaskId((int)taskId);
        StringBuilder builder=new StringBuilder();
        String contactStr="";
        if(contactses!=null&&contactses.size()>0){
            for(Contacts contact:contactses){
                builder.append(contact.getRealname());
                builder.append(CommonConstant.SPLIT_SIGN);
            }
            contactStr=builder.toString().substring(0,builder.length()-1);
        }
        historyDetail.setContacts(contactStr);
        alertHistoryDetailService.insert(historyDetail);
    }

    /**
     * 更新故障统计表
     * @param taskInfo 任务信息
     * @param rules 命中规则
     * @return 故障表数据id
     */
    private Integer updateAlertFault(AlertTaskInfo taskInfo,List<AlertRuleConstruct> rules,String ruleIds){
        //组织查询参数查询是否已有记录
        AlertFaultProcessor faultParam=new AlertFaultProcessor();
        faultParam.setAlertTaskId(taskInfo.getTask().getId());
        faultParam.setAlertDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        faultParam.setAlertRuleIds(ruleIds);
        List<AlertFaultProcessor> list=alertFaultProcessorService.getByObj(faultParam);
        Integer dataId;
        if (list==null||list.size()<1){
            //没有记录则插入新记录
            faultParam.setStatus(0);
            faultParam.setAppName(taskInfo.getTask().getAppName());
            StringBuilder build=new StringBuilder();
            for(AlertRuleConstruct rule:rules){
                build.append(rule.toString());
                build.append(System.lineSeparator());
            }
            faultParam.setTriggerRuleDesc(build.toString());
            faultParam.setAlertNum(1);
            alertFaultProcessorService.insert(faultParam);
            dataId=faultParam.getId();
        }else {
            //有记录则更新
            AlertFaultProcessor updateParam=list.get(0);
            Integer sum=1+updateParam.getAlertNum();
            updateParam.setAlertNum(sum);
            updateParam.setUpdateTime(Calendar.getInstance().getTime());
            alertFaultProcessorService.update(updateParam);
            dataId=updateParam.getId();
        }
        return dataId;
    }

}
