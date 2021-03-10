package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.bean.ExceptionFilterListDTO;
import com.rigai.rigeye.common.model.ExceptionContent;
import com.rigai.rigeye.common.service.ExceptionContentService;
import com.rigai.rigeye.common.dao.mysql.ExceptionFilterDao;
import com.rigai.rigeye.common.model.ExceptionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.ExceptionFilterService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("exceptionFilterService")
public class ExceptionFilterServiceImpl extends BaseBussinessImpl<ExceptionFilter> implements ExceptionFilterService {

	Logger logger= LoggerFactory.getLogger(ExceptionFilterServiceImpl.class);

	@Autowired
	private ExceptionFilterDao exceptionFilterDao;

	@Autowired
	private ExceptionContentService exceptionContentService;


	@Override
	public List<ExceptionFilterListDTO> getList(String user) {
		ExceptionFilter filterParam=new ExceptionFilter();
		filterParam.setUserId(user);
		List<ExceptionFilter> filterList=getByObj(filterParam);
		if(filterList==null||filterList.size()<1){
			return null;
		}
		ArrayList<ExceptionFilterListDTO> list=new ArrayList<>(filterList.size());
		filterList.parallelStream().forEach(filter->{
			ExceptionContent content=exceptionContentService.getById((long)filter.getFilterContentId());
			if(content!=null){
				ExceptionFilterListDTO exceptionFilterListDTO=new ExceptionFilterListDTO(content,filter);
				list.add(exceptionFilterListDTO);
			}else {
				logger.error("exception filter data error exception filter {} can't nest to any exception content",filter);
			}
		});
		return list;
	}
}
