package com.rigai.rigeye.web.bean.vo;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/24.
 */

public class AlertTaskSearchVO {

    private String appName;
    private String alertName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    @Override
    public String toString() {
        return "AlertTaskSearchVO{" +
                "appName='" + appName + '\'' +
                ", alertName='" + alertName + '\'' +
                '}';
    }
}
