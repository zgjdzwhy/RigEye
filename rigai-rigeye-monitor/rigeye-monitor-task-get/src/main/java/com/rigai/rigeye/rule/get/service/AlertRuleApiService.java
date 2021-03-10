package com.rigai.rigeye.rule.get.service;


import com.rigai.rigeye.common.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.common.bean.CommitTaskInfo;

import java.util.Map;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 * * 获取任务
 * 报警规则级别API 此处获取的应该是可直接执行的规则，后续如果从kafka获取则改变其实现
 */

public interface AlertRuleApiService {


    /**
     * 获得按照报警任务分组的报警规则
     * (考虑后续如果拆分服务理论上应该直接获得到CommitTaskInfo类型的数据，需要重新自行分组)
     * @return
     */
    public Map<AlertTaskDataConstruct,CommitTaskInfo> getCommitTask();
}
