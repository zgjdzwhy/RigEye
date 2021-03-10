package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.model.ExceptionFilter;
import com.rigai.rigeye.common.service.ExceptionFilterService;
import com.rigai.rigeye.common.dao.mysql.ExceptionContentDao;
import com.rigai.rigeye.common.model.ExceptionContent;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.ExceptionContentService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("exceptionKeywordService")
public class ExceptionContentServiceImpl extends BaseBussinessImpl<ExceptionContent> implements ExceptionContentService {

	@Autowired
	private ExceptionContentDao exceptionContentDao;

	@Autowired
	private ExceptionFilterService exceptionFilterService;

	@Override
	public void deleteExceptionContent(ExceptionContent exceptionContent) {
		delete(exceptionContent);
		ExceptionFilter exceptionFilter=new ExceptionFilter();
		exceptionFilter.setFilterContentId(exceptionContent.getId());
		exceptionFilterService.delete(exceptionFilter);
	}
}
