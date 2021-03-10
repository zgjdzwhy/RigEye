package com.rigai.rigeye.task.commit.bean.notice.mail;

import javax.validation.constraints.NotNull;


/**
 * @author chenxing
 */
public class MailInfo {
    private String receiver;
    /**
     *抄送人地址
     */
    private String copy;
    /**
     * 主题
    */
    private String subject;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MailInfo{" +
                "receiver='" + receiver + '\'' +
                ", copy='" + copy + '\'' +
                ", subject='" + subject + '\'' +
                ", title='" + title + '\'' +
                ", content='" + "节约空间，暂不显示" + '\'' +
                '}';
    }
}
