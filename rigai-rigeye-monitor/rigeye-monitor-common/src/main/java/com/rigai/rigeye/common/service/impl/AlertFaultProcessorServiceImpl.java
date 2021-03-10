package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.AlertFaultProcessorDao;
import com.rigai.rigeye.common.model.AlertFaultProcessor;
import com.rigai.rigeye.common.service.AlertFaultProcessorService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertFaultProcessorService")
public class AlertFaultProcessorServiceImpl extends BaseBussinessImpl<AlertFaultProcessor> implements AlertFaultProcessorService {

	@Autowired
	private AlertFaultProcessorDao alertFaultProcessorDao;

}
