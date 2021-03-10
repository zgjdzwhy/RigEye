package com.rigai.rigeye.common.dao.mysql;

import com.rigai.rigeye.common.model.DataSetRule;
import com.em.fx.core.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
@Mapper
public interface DataSetRuleDao extends BaseDao<DataSetRule> {
    int save(DataSetRule dataSetRule);
    int updateDataSet(DataSetRule dataSetRule);
    List<DataSetRule> selectByPage(DataSetRule dataSetRule);
}
