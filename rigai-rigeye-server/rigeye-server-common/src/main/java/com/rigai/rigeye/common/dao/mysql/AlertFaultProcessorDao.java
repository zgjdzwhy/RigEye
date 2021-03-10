package com.rigai.rigeye.common.dao.mysql;

import com.rigai.rigeye.common.bean.AlertProcessSearchParamVO;
import com.rigai.rigeye.common.dto.AlertFaultAndAlertTask;
import org.apache.ibatis.annotations.Mapper;
import com.em.fx.core.dao.BaseDao;
import com.rigai.rigeye.common.model.AlertFaultProcessor;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Mapper
public interface AlertFaultProcessorDao extends BaseDao<AlertFaultProcessor> {
    List<AlertFaultAndAlertTask> getAlertFaultAgg(AlertProcessSearchParamVO alertFaultAndAlertTask);
}
