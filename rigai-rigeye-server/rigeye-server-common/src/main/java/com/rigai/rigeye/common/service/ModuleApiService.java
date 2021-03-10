package com.rigai.rigeye.common.service;

import java.util.Set;

/**
 * @author chenxing
 * Created by ChenXing on 2018/10/11.
 *
 */

public interface ModuleApiService {
    /**
     * 根据用户信息获取用户所能访问的模块集合
     * @param userId 工号
     * @return 用户能访问的模块集合，异常则返回空
     */
    Set<String> getUserModule(String userId);
}
