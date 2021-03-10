package com.rigai.rigeye.common.service.impl;

import com.rigai.rigeye.common.dao.mysql.DataSetRuleDao;
import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.common.service.DataSetRuleService;
import com.em.fx.core.bussiness.impls.BaseBussinessImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Admin
 * @date 2018/08/01
 */
@Service("dataSetRuleService")
public class DataSetRuleServiceImpl extends BaseBussinessImpl<DataSetRule> implements DataSetRuleService {

    @Autowired
    private DataSetRuleDao dataSetRuleDao;

    @Override
    public int save(DataSetRule dataSetRule) {
        return dataSetRuleDao.save(dataSetRule);
    }

    @Override
    public int updateDataSet(DataSetRule dataSetRule) {
        return dataSetRuleDao.updateDataSet(dataSetRule);
    }

    @Override
    public PageInfo<DataSetRule> selectByPage(DataSetRule dataSetRule,int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(dataSetRuleDao.selectByPage(dataSetRule));
    }
}
