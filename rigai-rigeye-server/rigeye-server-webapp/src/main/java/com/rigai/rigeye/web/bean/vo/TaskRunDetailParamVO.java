package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author yh
 * @date 2018/9/4 16:40
 */
public class TaskRunDetailParamVO {
    @NotNull
    private Integer intervalInSec;
    @NotNull
    private Long maxTime;
    @NotNull
    private Long minTime;
    @NotNull
    private Integer step;
    @NotNull
    private Long taskId;

    public Integer getIntervalInSec() {
        return intervalInSec;
    }

    public void setIntervalInSec(Integer intervalInSec) {
        this.intervalInSec = intervalInSec;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
