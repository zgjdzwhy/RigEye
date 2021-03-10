package com.rigai.rigeye.task.get.service;


import com.rigai.rigeye.common.bean.AlertHistoryDetailInfo;
import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.bean.AlertRuleConstruct;
import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.common.model.AlertRule;
import com.rigai.rigeye.common.model.AlertTask;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/8/21.
 *
 * 获取任务
 * 报警任务级别API 后续如果使用调度平台定时调用则可单独分出一个项目
 */

public interface AlertApiService {

    List<AlertTaskInfo> getAllAlertTask();
    List<AlertRuleConstruct> toAlertRuleConstructList(List<AlertRule> rules);
    List<AlertRule> toAlertRule(List<AlertRuleConstruct> ruleConstructs, Long taskId, String user);
    AlertTaskInfo getAlertTask(Long taskId, String user);
    List<AlertTask> getAll();
    PageInfo<AlertHistoryDetailInfo> pageGetAlertHistoryDetailInfo(AlertHistoryQueryParam info, Integer page, Integer pageSize);
}
