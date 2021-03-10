package com.rigai.rigeye.web.bean.vo;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 * @date 2018/8/13 11:27
 */
public class DataSetModelVO implements Serializable {
    @Valid
    private List<DataSetClearModelVO> model;

    public List<DataSetClearModelVO> getModel() {
        return model;
    }

    public void setModel(List<DataSetClearModelVO> model) {
        this.model = model;
    }
}

