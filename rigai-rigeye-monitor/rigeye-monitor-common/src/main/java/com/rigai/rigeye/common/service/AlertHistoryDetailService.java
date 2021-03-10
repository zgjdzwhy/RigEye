package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.em.fx.core.bussiness.BaseBussiness;
import com.github.pagehelper.PageInfo;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface AlertHistoryDetailService extends BaseBussiness<AlertHistoryDetail> {

    PageInfo<AlertHistoryDetail> pageQueryByAlertHistoryDetailInfo(AlertHistoryQueryParam param, Integer page, Integer pageSize);
}
