package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.bean.ExceptionFilterListDTO;
import com.em.fx.core.bussiness.BaseBussiness;
import com.rigai.rigeye.common.model.ExceptionFilter;

import java.util.List;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface ExceptionFilterService extends BaseBussiness<ExceptionFilter> {
    List<ExceptionFilterListDTO> getList(String user);
}
