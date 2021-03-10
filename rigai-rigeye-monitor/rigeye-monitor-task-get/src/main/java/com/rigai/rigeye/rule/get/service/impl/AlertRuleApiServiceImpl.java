package com.rigai.rigeye.rule.get.service.impl;

import com.rigai.rigeye.common.bean.AlertTaskDataConstruct;
import com.rigai.rigeye.common.bean.CommitTaskInfo;
import com.rigai.rigeye.rule.get.service.AlertRuleApiService;
import com.rigai.rigeye.task.get.service.AlertApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.

 */

@Component
public class AlertRuleApiServiceImpl implements AlertRuleApiService{
    @Autowired
    AlertApiService alertApiService;

    @Override
    public Map<AlertTaskDataConstruct, CommitTaskInfo> getCommitTask() {
        return null;
    }
}
