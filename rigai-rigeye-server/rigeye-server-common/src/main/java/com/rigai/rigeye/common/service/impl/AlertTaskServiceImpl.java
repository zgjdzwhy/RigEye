package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.AlertTaskDao;
import com.rigai.rigeye.common.model.AlertTask;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.AlertTaskService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertTaskService")
public class AlertTaskServiceImpl extends BaseBussinessImpl<AlertTask> implements AlertTaskService {

	@Autowired
	private AlertTaskDao alertTaskDao;


	@Override
	public List<AlertTask> listAlertTask(AlertTask param, Set<String> modules) {
		if(modules!=null){
			return alertTaskDao.listAlertTask(param,new ArrayList<>(modules));
		}
		else {
			return alertTaskDao.listAlertTask(param,null);
		}
	}
}
