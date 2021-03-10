package com.rigai.rigeye.task.commit.bean.notice.sms;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chenxing on 2016/9/1.
 * 公司短信平台2.0回送参数，备用
 */
public class SMSResponseEntity {
    @JsonProperty(value = "Status")
    private int status;
    @JsonProperty(value = "MessageId")
    private String messageId;
    @JsonProperty(value = "Message")
    private String message;

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

    @Override
    public String toString() {
        return "SMSResponseEntity{" +
                "status=" + status +
                ", messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
