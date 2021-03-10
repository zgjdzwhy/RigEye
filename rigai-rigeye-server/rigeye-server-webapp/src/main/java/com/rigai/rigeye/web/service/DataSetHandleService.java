package com.rigai.rigeye.web.service;

import com.rigai.rigeye.common.model.DataSetRule;
import com.rigai.rigeye.web.bean.vo.DataSetCheckVO;
import com.rigai.rigeye.web.bean.vo.DataSetModelVO;
import com.rigai.rigeye.web.bean.vo.DataSetOperationVO;
import com.em.fx.common.bean.Result;

import java.util.List;


/**
 * @author yh
 * @date 2018/8/13 15:04
 */
public interface DataSetHandleService  {
    DataSetOperationVO chartConfig(DataSetModelVO model);

    List<DataSetCheckVO> getDataSetChecks(Long taskId);

    Result saveOrUpdate(DataSetRule dataSetRule);
}
