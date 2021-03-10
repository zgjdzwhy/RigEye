package com.rigai.rigeye.task.commit.influx;

import com.rigai.rigeye.common.bean.AlertDataSetConstruct;
import com.rigai.rigeye.common.bean.AlertRuleConstruct;
import com.rigai.rigeye.common.bean.CommitTaskInfo;
import com.rigai.rigeye.common.bean.TaskResult;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/11.
 */

public interface InfluxService {

    /**
     * 指标查询，根据预警规则查询influxDB中数据
     * @param info 预警规则
     * @return 符合则accord字段为true，不符合则为false
     */
    public TaskResult indexQuery(CommitTaskInfo info);

    public double indexQuery(AlertDataSetConstruct dataSetConstruct, AlertRuleConstruct rule);
}
