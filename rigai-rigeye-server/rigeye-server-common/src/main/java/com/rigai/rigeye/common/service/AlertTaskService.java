package com.rigai.rigeye.common.service;

import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.AlertTask;

import java.util.List;
import java.util.Set;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface AlertTaskService extends BaseBussiness<AlertTask> {

    List<AlertTask> listAlertTask(AlertTask param, Set<String> modules);
}
