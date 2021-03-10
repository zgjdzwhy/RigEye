package com.rigai.rigeye.task.commit.bean.notice.mail;/**
 * Created by Administrator on 2015/12/3.
 */

/**
 * @autho 陈兴
 * 2015/12/3
 */
public class BaseResult<T extends Object>{
    private Boolean success=false;
    private T resultMessage=null;

    public BaseResult() {
    }

    public BaseResult(Boolean success, T resultMessage) {
        this.success = success;
        this.resultMessage = resultMessage;
    }

    public T getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(T resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", resultMessage=" + resultMessage.toString() +
                '}';
    }
}
