package com.rigai.rigeye.common.dao.mysql;

import com.rigai.rigeye.common.bean.AlertHistoryQueryParam;
import com.rigai.rigeye.common.model.AlertHistoryDetail;
import com.em.fx.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Mapper
public interface AlertHistoryDetailDao extends BaseDao<AlertHistoryDetail> {


    /**
     * 根据参数查询，不查询非空字段
     * @param param 接收参数
     * @return 返回列表
     */
    List<AlertHistoryDetail> queryByAlertHistoryDetailInfo(AlertHistoryQueryParam param);
}
