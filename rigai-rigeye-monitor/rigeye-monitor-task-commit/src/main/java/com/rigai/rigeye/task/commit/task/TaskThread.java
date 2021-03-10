package com.rigai.rigeye.task.commit.task;

import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.task.commit.service.CommitAlertTaskService;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/12.
 * 预警任务级别线程
 */

public class TaskThread implements Runnable{

    private CommitAlertTaskService commitAlertTaskService;
    private AlertTaskInfo taskInfo;

    public TaskThread(CommitAlertTaskService commitAlertTaskService, AlertTaskInfo taskInfo) {
        this.commitAlertTaskService = commitAlertTaskService;
        this.taskInfo = taskInfo;
    }

    @Override
    public void run() {
        commitAlertTaskService.commitTask(taskInfo);
    }
}
