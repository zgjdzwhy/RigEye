package com.rigai.rigeye.alert.bean;


import javax.validation.Valid;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/21.
 */

public class AlertTaskInfo {
   // @NotNull
    @Valid
    private AlertTaskDataConstruct task;
    @Valid
    private List<AlertRuleConstruct> rules;

    public AlertTaskInfo() {
    }

    public AlertTaskInfo(AlertTaskDataConstruct task, List<AlertRuleConstruct> rules) {
        this.task = task;
        this.rules = rules;
    }

    public AlertTaskDataConstruct getTask() {
        return task;
    }

    public void setTask(AlertTaskDataConstruct task) {
        this.task = task;
    }

    public List<AlertRuleConstruct> getRules() {
        return rules;
    }

    public void setRules(List<AlertRuleConstruct> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "AlertTaskInfo{" +
                "task=" + task +
                ", rules=" + rules +
                '}';
    }
}

