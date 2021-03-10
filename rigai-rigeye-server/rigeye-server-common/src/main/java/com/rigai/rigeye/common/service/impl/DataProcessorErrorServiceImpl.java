package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.DataProcessorErrorDao;
import com.rigai.rigeye.common.model.DataProcessorError;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.DataProcessorErrorService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("dataProcessorErrorService")
public class DataProcessorErrorServiceImpl extends BaseBussinessImpl<DataProcessorError> implements DataProcessorErrorService {

	@Autowired
	private DataProcessorErrorDao dataProcessorErrorDao;


}
