package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.model.DataSetRule;
import com.em.fx.core.bussiness.BaseBussiness;
import com.github.pagehelper.PageInfo;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface DataSetRuleService extends BaseBussiness<DataSetRule> {
   int save(DataSetRule dataSetRule);
   int updateDataSet(DataSetRule dataSetRule);
   PageInfo<DataSetRule> selectByPage(DataSetRule dataSetRule,int page, int pageSize);

}
