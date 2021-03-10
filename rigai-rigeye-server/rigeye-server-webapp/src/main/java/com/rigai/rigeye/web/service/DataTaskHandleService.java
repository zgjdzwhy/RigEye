package com.rigai.rigeye.web.service;

import com.rigai.rigeye.web.bean.vo.DataTaskCopyVO;
import com.em.fx.common.bean.Result;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/22 16:50
 */
public interface DataTaskHandleService {
    Result startOne(Long id, String offset, String userId) throws Exception;
    Result stopOne(Long id, String userId);
    Result deleteOne(Long id,String userId);
    Result findStatus(String userId, List<Long> searchIds);
    Result copyTask(DataTaskCopyVO copyVO, String userId);
}
