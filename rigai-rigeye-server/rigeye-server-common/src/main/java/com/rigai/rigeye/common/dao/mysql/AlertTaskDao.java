package com.rigai.rigeye.common.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import com.em.fx.core.dao.BaseDao;
import com.rigai.rigeye.common.model.AlertTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Admin
 */
@Mapper
public interface AlertTaskDao extends BaseDao<AlertTask> {


    List<AlertTask> listAlertTask(@Param("alertTask") AlertTask alertTask, @Param("modules") List<String> modules);
}
