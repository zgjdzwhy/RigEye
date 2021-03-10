package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.bean.CommitTaskInfo;
import com.sun.tools.javac.util.List;

/**
 * @author chenxing
 * Created by ChenXing on 2018/9/11.
 *
 * 获取任务
 * 报警规则级别API 后续如果使用分布式部署从kafka获取报警规则则改写实现
 */

public interface AlertRuleApiService {

    public List<CommitTaskInfo> get();
}
