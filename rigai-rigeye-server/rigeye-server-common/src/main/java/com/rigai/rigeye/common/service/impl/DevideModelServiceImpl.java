package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.DevideModelDao;
import com.rigai.rigeye.common.model.DevideModel;
import com.rigai.rigeye.common.service.DevideModelService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("devideModelService")
public class DevideModelServiceImpl extends BaseBussinessImpl<DevideModel> implements DevideModelService {

	@Autowired
	private DevideModelDao devideModelDao;


}
