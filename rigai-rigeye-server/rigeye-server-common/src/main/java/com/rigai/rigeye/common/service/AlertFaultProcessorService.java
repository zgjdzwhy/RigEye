package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.bean.AlertProcessSearchParamVO;
import com.rigai.rigeye.common.dto.AlertFaultAndAlertTask;
import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.AlertFaultProcessor;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface AlertFaultProcessorService extends BaseBussiness<AlertFaultProcessor> {
    List<AlertFaultAndAlertTask> getAlertFaultAgg(AlertProcessSearchParamVO alertProcessSearchParamVO);
}
