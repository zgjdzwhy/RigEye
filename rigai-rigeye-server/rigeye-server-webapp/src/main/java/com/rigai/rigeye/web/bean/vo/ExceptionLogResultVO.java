package com.rigai.rigeye.web.bean.vo;

/**
 * @author yh
 * @date 2018/9/7 15:35
 */
public class ExceptionLogResultVO {
    private String appName;
    private String level;
    private String templateName;
    private long total;

    public ExceptionLogResultVO(String appName, String level, String templateName, long total) {
        this.appName = appName;
        this.level = level;
        this.templateName = templateName;
        this.total = total;
    }

    public ExceptionLogResultVO() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
