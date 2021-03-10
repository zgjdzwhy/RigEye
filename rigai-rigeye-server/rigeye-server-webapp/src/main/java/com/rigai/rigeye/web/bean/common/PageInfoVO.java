package com.rigai.rigeye.web.bean.common;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/6.
 */

public class PageInfoVO<T> {

    @NotNull
    @Min(value = 1)
    private Integer pageSize;
    @NotNull
    @Min(value = 1)
    private Integer page;
    @Valid
    private T info;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    @Override
    public String toString() {
        String temp="PageInfoVO{" +
                "pageSize=" + pageSize +
                ", page=" + page ;
        if(info!=null){
            return temp+"info="+info.toString();
        }else {
            return temp;
        }
    }
}
