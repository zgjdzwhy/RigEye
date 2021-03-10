package com.rigai.rigeye.web.service;

import com.rigai.rigeye.web.bean.vo.DataSetAggrateParamVO;
import com.rigai.rigeye.web.bean.vo.ExceptionLogParamVO;
import com.rigai.rigeye.web.bean.vo.LogDetailParamVO;
import com.rigai.rigeye.web.bean.vo.TaskRunDetailParamVO;
import com.em.fx.common.bean.Result;

/**
 * @author yh
 * @date 2018/9/5 11:17
 */
public interface DetailSearchService {
    Result searchTaskRunDetail(TaskRunDetailParamVO taskRunDetailParamVO);
    Result searchBadDivideDetail(TaskRunDetailParamVO taskRunDetailParamVO);
    Result searchExceptionLogList(ExceptionLogParamVO exceptionLogParamVO);
    Result searchLogDetail(LogDetailParamVO logDetailParamVO);
    Result searchDataSetAggrationDetail(DataSetAggrateParamVO dataSetAggrateParamVO);
}
