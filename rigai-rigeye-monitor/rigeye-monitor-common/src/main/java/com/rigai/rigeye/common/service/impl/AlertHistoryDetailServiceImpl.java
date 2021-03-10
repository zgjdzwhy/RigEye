package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.dao.mysql.AlertHistoryDetailDao;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.rigai.rigeye.common.service.AlertHistoryDetailService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("alertHistoryDetailService")
public class AlertHistoryDetailServiceImpl extends BaseBussinessImpl<AlertHistoryDetail> implements AlertHistoryDetailService {

	@Autowired
	private AlertHistoryDetailDao alertHistoryDetailDao;


	@Override
	public PageInfo<AlertHistoryDetail> pageQueryByAlertHistoryDetailInfo(AlertHistoryQueryParam param, Integer page, Integer pageSize) {
		PageHelper.startPage(page,pageSize);
		List<AlertHistoryDetail> details=alertHistoryDetailDao.queryByAlertHistoryDetailInfo(param);
		return new PageInfo<>(details);
	}
}
