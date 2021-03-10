package com.rigai.rigeye.common.service;

import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.DataTask;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface DataTaskService extends BaseBussiness<DataTask> {
    List<String> findDataTaskNameByUserId(String userId);

    List<String> findAppNameByUserId(String userId);

    List<DataTask> findDataTaskByUserIdAndIdIn(String userId, List<Long> ids);

    PageInfo<DataTask> pageFindList(DataTask dataTask, Integer page, Integer pageSize,String user);
}
