package com.rigai.rigeye.web.bean.vo;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/13 16:23
 */
public class DataSetClearModelVO  implements Serializable {
    @NotEmpty
    private String name;
    @NotEmpty
    private String type;

    public DataSetClearModelVO() {
    }

    public DataSetClearModelVO(@NotEmpty String name, @NotEmpty String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
