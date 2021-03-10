package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.dto.DataSourceDTO;
import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.DataSource;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface DataSourceService extends BaseBussiness<DataSource> {
    DataSourceDTO getDataSourceDTOById(Long id);

    PageInfo<DataSourceDTO> pageGetDataSourceDTO(DataSource param, Integer page, Integer pageSize);

    List<DataSourceDTO> getDTOByObj(DataSource dataSource);
}
