package com.rigai.rigeye.task.commit.bean.notice.sms;

/**
 * Created by chenxing on 2016/9/1.
 */

/**
 * 大数据部短信平台的返回消息
 */
public class SMSResult {
    private int status;
    private String messageId;
    private String message;
    private boolean success=false;

    public SMSResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "SMSResult{" +
                "status=" + status +
                ", messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
