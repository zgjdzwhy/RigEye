package com.rigai.rigeye.web.bean.vo;

/**
 * @author yh
 * @date 2018/8/15 10:39
 */
public class DataSetCheckVO {
    private Long id;
    private String name;
    //是否可以编辑
    private boolean editable;
    //出错提示
    private String error;
    //数据集类型
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public DataSetCheckVO() {
    }

    public DataSetCheckVO(Long id, String name, boolean editable,Integer status, String error) {
        this.id = id;
        this.name = name;
        this.editable = editable;
        this.status = status;
        this.error = error;
    }

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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
