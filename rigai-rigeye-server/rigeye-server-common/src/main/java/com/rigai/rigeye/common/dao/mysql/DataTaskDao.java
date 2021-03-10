package com.rigai.rigeye.common.dao.mysql;

import org.apache.ibatis.annotations.Mapper;
import com.em.fx.core.dao.BaseDao;
import com.rigai.rigeye.common.model.DataTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Mapper
public interface DataTaskDao extends BaseDao<DataTask> {
    List<String> findDataTaskNameByUserId(String userId);
    List<String> findAppNameByUserId(String userId);
    List<DataTask> findByUserIdAndIdIn(@Param("userId") String userId,@Param("list") List<Long> list);
    List<DataTask> pageFindList(@Param("task") DataTask dataTask,@Param("page") Integer page,@Param("pageSize") Integer pageSize,@Param("modules") List<String> modules);
}
