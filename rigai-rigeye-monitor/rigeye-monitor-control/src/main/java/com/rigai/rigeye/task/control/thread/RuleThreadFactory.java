package com.rigai.rigeye.task.control.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/11.
 */

public class RuleThreadFactory implements ThreadFactory{

    private static final String NAME_PREFIX="aegis-rule-thread";

    private ThreadGroup threadGroup;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public RuleThreadFactory(){
        threadGroup=Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(threadGroup, r,
                NAME_PREFIX + threadNumber.getAndIncrement(),
                0);
        if (thread.isDaemon()){
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY){
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
