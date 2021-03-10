package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotNull;

/**
 * @author yh
 * @date 2018/8/23 16:58
 */
public class LogCrawParamVO {
    @NotNull
    private Integer type;
    @NotNull
    private Integer id;
    @NotNull
    private String encode;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }
}
