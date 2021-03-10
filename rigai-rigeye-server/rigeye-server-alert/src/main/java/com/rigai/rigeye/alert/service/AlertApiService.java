package com.rigai.rigeye.alert.service;



import com.rigai.rigeye.alert.bean.*;
import com.rigai.rigeye.alert.bean.AlertHistoryDetailInfo;
import com.rigai.rigeye.alert.bean.AlertRuleConstruct;
import com.rigai.rigeye.alert.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.alert.bean.AlertTaskInfo;
import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.model.AlertRule;
import com.rigai.rigeye.common.model.AlertTask;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/21.
 */

public interface AlertApiService {
    boolean addAlertTask(AlertTaskDataConstruct alertDataSetConstruct, List<AlertRuleConstruct> ruleConstructs, String user);
    boolean editAlertTask(AlertTaskDataConstruct alertDataConstruct, List<AlertRuleConstruct> ruleConstructs, String user);
    PageInfo<AlertTaskInfo> pageGetAlertTask(int page, int pageSize, AlertTask param, String trueUser);
    List<AlertRuleConstruct> toAlertRuleConstructList(List<AlertRule> rules);
    List<AlertRule> toAlertRule(List<AlertRuleConstruct> ruleConstructs,Long taskId,String user);
    boolean deleteAlertTask(Long taskId,String user);
    AlertTaskInfo getAlertTask(Long taskId,String user);
    List<AlertTask> getAll();
    PageInfo<AlertHistoryDetailInfo> pageGetAlertHistoryDetailInfo(AlertHistoryQueryParam info, Integer page, Integer pageSize);

    void stopTask(Long taskId);

    void startTask(Long taskId);
}
