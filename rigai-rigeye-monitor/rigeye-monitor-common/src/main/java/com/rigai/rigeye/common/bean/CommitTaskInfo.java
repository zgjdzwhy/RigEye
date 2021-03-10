package com.rigai.rigeye.common.bean;



/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/11.
 */

public class CommitTaskInfo {
    private AlertTaskDataConstruct task;
    private AlertRuleConstruct rule;

    public CommitTaskInfo() {
    }

    public CommitTaskInfo(AlertTaskDataConstruct task, AlertRuleConstruct rule) {
        this.task = task;
        this.rule = rule;
    }

    public AlertTaskDataConstruct getTask() {
        return task;
    }

    public void setTask(AlertTaskDataConstruct task) {
        this.task = task;
    }

    public AlertRuleConstruct getRule() {
        return rule;
    }

    public void setRule(AlertRuleConstruct rule) {
        this.rule = rule;
    }
}
