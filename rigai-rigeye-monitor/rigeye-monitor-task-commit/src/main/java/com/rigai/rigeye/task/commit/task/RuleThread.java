package com.rigai.rigeye.task.commit.task;


import com.rigai.rigeye.common.bean.CommitTaskInfo;
import com.rigai.rigeye.common.bean.TaskResult;
import com.rigai.rigeye.task.commit.influx.InfluxService;
import com.em.fx.redis.dao.RedisDao;


/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 * 规则级别线程。较为复杂，暂时不用
 */

public class RuleThread implements Runnable {


    private CommitTaskInfo commitTaskInfo;

    private InfluxService influxService;

    private RedisDao redisDao;

    /**
     * 时间戳用于redis识别
     */
    private Long timestamp;

    /**
     * 多线程下无法注入，需要用构造函数传递
     */
    public RuleThread(CommitTaskInfo commitTaskInfo, InfluxService influxService, RedisDao redisDao, Long timestamp) {
        this.commitTaskInfo = commitTaskInfo;
        this.influxService = influxService;
        this.redisDao=redisDao;
        this.timestamp=timestamp;
    }


    @Override
    public void run() {
        //TODO 判断该条规则是否满足
        TaskResult result=influxService.indexQuery(commitTaskInfo);
        // TODO 修改redis中的 TaskFlag
    }
}
