package com.rigai.rigeye.common.service;

import com.rigai.rigeye.common.model.ExceptionContent;
import com.em.fx.core.bussiness.BaseBussiness;

/**
 * @author Admin
 * @date 2018/08/01
 */
public interface ExceptionContentService extends BaseBussiness<ExceptionContent> {

    void deleteExceptionContent(ExceptionContent exceptionContent);
}
