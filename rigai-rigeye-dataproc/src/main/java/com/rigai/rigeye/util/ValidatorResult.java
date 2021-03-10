package com.rigai.rigeye.util;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yh
 * @date 2018/9/3 10:57
 */
public class ValidatorResult implements Serializable {
    private boolean hasErrors;
    private Map<String,String> errorMsg;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Map<String, String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
