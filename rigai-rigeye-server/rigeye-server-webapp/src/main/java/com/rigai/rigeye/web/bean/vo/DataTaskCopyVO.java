package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author yh
 * @date 2018/8/24 17:06
 */
public class DataTaskCopyVO {
    @NotNull
    private Long id;
    @NotEmpty(message = "任务名称不能为空!")
    private String name;
    @NotEmpty(message = "绑定的应用名称不能为空!")
    private String appName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
