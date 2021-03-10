package com.rigai.rigeye.task.commit.bean.notice.sms;

/**
 * 短信消息类
 * 对应适用于大数据部短信平台的接收参数
 */
public class ShotMessageInfo {
    /**
     * 短信号码，多个号码间以逗号隔开
     */
    private String phoneNumbers;
    /**
     * 短信内容，不能包含中文中括号，对应大数据短信平台时平台会自动替换中文中括号为英文中括号，故发送方不用处理
     */
    private String content;

    private String origin="rigai-rigeye-monitor";

    public ShotMessageInfo() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "ShotMessageInfo{" +
                "phoneNumbers='" + phoneNumbers + '\'' +
                ", content='" + content + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}