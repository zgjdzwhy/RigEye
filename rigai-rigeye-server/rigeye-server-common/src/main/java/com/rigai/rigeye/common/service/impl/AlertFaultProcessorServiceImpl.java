package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.bean.AlertProcessSearchParamVO;
import com.rigai.rigeye.common.dto.AlertFaultAndAlertTask;
import com.rigai.rigeye.common.dao.mysql.AlertFaultProcessorDao;
import com.rigai.rigeye.common.model.AlertFaultProcessor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.AlertFaultProcessorService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertFaultProcessorService")
public class AlertFaultProcessorServiceImpl extends BaseBussinessImpl<AlertFaultProcessor> implements AlertFaultProcessorService {

	@Autowired
	private AlertFaultProcessorDao alertFaultProcessorDao;

	@Override
	public List<AlertFaultAndAlertTask> getAlertFaultAgg(AlertProcessSearchParamVO alertProcessSearchParamVO) {
		return alertFaultProcessorDao.getAlertFaultAgg(alertProcessSearchParamVO);
	}


}
