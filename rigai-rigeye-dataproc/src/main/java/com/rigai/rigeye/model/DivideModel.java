package com.rigai.rigeye.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/20 14:32
 */
public class DivideModel implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String type;

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
