package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.AlertRuleDao;
import com.rigai.rigeye.common.model.AlertRule;
import com.rigai.rigeye.common.service.AlertRuleService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertRuleService")
public class AlertRuleServiceImpl extends BaseBussinessImpl<AlertRule> implements AlertRuleService {

	@Autowired
	private AlertRuleDao alertRuleDao;


}
