package com.rigai.rigeye.task.commit.service;

import com.rigai.rigeye.common.bean.AlertRuleConstruct;
import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.common.model.Contacts;

import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/12.
 */

public interface AlertChannelService {
    void alert(AlertTaskInfo taskInfo, List<AlertRuleConstruct> rule,List<Contacts> contacts);

    void alert(AlertTaskInfo taskInfo,List<Contacts> contacts);

    void recoverAlert(AlertTaskInfo taskInfo,List<Contacts> contacts);

    void recoverAlert(AlertTaskInfo taskInfo, List<AlertRuleConstruct> ruleConstructs,List<Contacts> contacts);
}
