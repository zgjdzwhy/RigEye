package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.AlertTaskDao;
import com.rigai.rigeye.common.model.AlertTask;
import com.rigai.rigeye.common.service.AlertTaskService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertTaskService")
public class AlertTaskServiceImpl extends BaseBussinessImpl<AlertTask> implements AlertTaskService {

	@Autowired
	private AlertTaskDao alertTaskDao;


}
