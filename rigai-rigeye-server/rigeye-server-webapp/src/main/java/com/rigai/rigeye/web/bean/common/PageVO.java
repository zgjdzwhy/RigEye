package com.rigai.rigeye.web.bean.common;

import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/6.
 */

public class PageVO<T> {
    private List<T> list;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 总数据条数
     */
    private Long total;

    public PageVO() {
    }

    public PageVO(List<T> list, Integer pages, Long total) {
        this.list = list;
        this.pages = pages;
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageVO{" +
                "list=" + list +
                ", pages=" + pages +
                ", total=" + total +
                '}';
    }
}
