package com.rigai.rigeye.task.control.schedule;

import com.rigai.rigeye.common.bean.AlertTaskInfo;
import com.rigai.rigeye.task.commit.service.CommitAlertTaskService;
import com.rigai.rigeye.task.commit.task.TaskThread;
import com.rigai.rigeye.task.get.service.AlertApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 */
@Component
@EnableScheduling
public class AlertSchedule {

    @Autowired
    AlertApiService alertApiService;

    @Autowired
    CommitAlertTaskService commitAlertTaskService;

    @Resource(name = "taskExecutor")
    ExecutorService taskExecutor;

    private Logger logger= LoggerFactory.getLogger(AlertSchedule.class);

    @Scheduled(fixedRate = 60000)
    public void alertTask(){
        logger.info("===========================================");
        logger.info("scheduled task begin");
        logger.info("===========================================");
        List<AlertTaskInfo> tasks=alertApiService.getAllAlertTask();
        tasks.parallelStream().forEach(task->{
                    TaskThread taskThread=new TaskThread(commitAlertTaskService,task);
                    taskExecutor.execute(taskThread);
                }
        );
    }
}
