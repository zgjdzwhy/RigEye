package com.rigai.rigeye.common.bean;


/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 */

public class TaskResult {
    private boolean accord;
    private AlertTaskDataConstruct task;
    private AlertRuleConstruct rule;

    public boolean isAccord() {
        return accord;
    }

    public void setAccord(boolean accord) {
        this.accord = accord;
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

    @Override
    public String toString() {
        return "TaskResult{" +
                "accord=" + accord +
                ", task=" + task +
                ", rule=" + rule +
                '}';
    }
}
