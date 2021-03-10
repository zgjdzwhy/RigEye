package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.config.AuthorizationConfig;
import com.rigai.rigeye.common.service.ModuleApiService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rigai.rigeye.common.dao.mysql.DataTaskDao;
import com.rigai.rigeye.common.model.DataTask;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.rigai.rigeye.common.service.DataTaskService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("dataTaskService")
public class DataTaskServiceImpl extends BaseBussinessImpl<DataTask> implements DataTaskService {

	@Autowired
	private DataTaskDao dataTaskDao;

	@Resource
	ModuleApiService moduleApiService;

	@Override
	public List<String> findDataTaskNameByUserId(String userId) {
		return dataTaskDao.findDataTaskNameByUserId(userId);
	}

	@Override
	public List<String> findAppNameByUserId(String userId) {
		return dataTaskDao.findAppNameByUserId(userId);
	}

	@Override
	public List<DataTask> findDataTaskByUserIdAndIdIn(String userId, List<Long> list) {
        return dataTaskDao.findByUserIdAndIdIn(userId, list);
	}

	@Override
	public PageInfo<DataTask> pageFindList(DataTask dataTask, Integer page, Integer pageSize,String user) {
		Set<String> modules=null;
		if(user!=null&& AuthorizationConfig.isAuthorizationEnable()){
			if(!AuthorizationConfig.isAdmin(user)){
				modules=moduleApiService.getUserModule(user);
				logger.info("user {} access module: {}",user,modules);
				if(modules==null||modules.size()<1){
					return null;
				}
			}
		}
		logger.info("user {} access module: {}",user,modules);
		PageHelper.startPage(page, pageSize);
		if(modules!=null){
			return new PageInfo<>(dataTaskDao.pageFindList(dataTask, page, pageSize,new ArrayList<>(modules)));
		}else {
			return new PageInfo<>(dataTaskDao.pageFindList(dataTask, page, pageSize,null));
		}
	}
}
