package com.rigai.rigeye.task.control.executor;

import com.rigai.rigeye.task.control.thread.TaskThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 */

@Configuration
public class ExecutorCreator {

   /* @Value("${rule.thread.pool.core}")
    private int ruleThreadPoolCoreSize;
    @Value("${rule.thread.pool.max}")
    private int ruleThreadPoolMaxSize;
    @Value("${rule.thread.pool.alive.time}")
    private Long ruleThreadPoolAliveTime;*/

    @Value("${task.thread.pool.core}")
    private int taskThreadPoolCoreSize;
    @Value("${task.thread.pool.max}")
    private int taskThreadPoolMaxSize;
    @Value("${task.thread.pool.alive.time}")
    private Long taskThreadPoolAliveTime;


    /*@PostConstruct
    @Bean(name = "ruleExecutor")
    public ExecutorService createRuleExecutor(){
        return new ThreadPoolExecutor(ruleThreadPoolCoreSize, ruleThreadPoolMaxSize,
                ruleThreadPoolAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new RuleThreadFactory());

    }*/

    @PostConstruct
    @Bean(name = "taskExecutor")
    public ExecutorService createTaskExecutor(){
        return new ThreadPoolExecutor(taskThreadPoolCoreSize, taskThreadPoolMaxSize,
                taskThreadPoolAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new TaskThreadFactory());

    }

    /*@PreDestroy
    public void close(@Qualifier("taskExecutor") ExecutorService taskExecutor){
        taskExecutor.shutdown();
    }*/
}
