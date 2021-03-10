package com.rigai.rigeye.task.commit.bean.notice.weixin;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/9/14.
 */

public class WeiXinReqInfo {
    private String tousers;
    private String content;

    public WeiXinReqInfo(String tousers, String content) {
        this.tousers = tousers;
        this.content = content;
    }

    public String getTousers() {
        return tousers;
    }

    public void setTousers(String tousers) {
        this.tousers = tousers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WeiXinReqInfo{" +
                "tousers='" + tousers + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
