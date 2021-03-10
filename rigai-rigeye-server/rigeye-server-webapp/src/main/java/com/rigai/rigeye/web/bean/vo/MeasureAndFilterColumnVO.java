package com.rigai.rigeye.web.bean.vo;

import java.util.List;

/**
 * @author yh
 * @date 2018/8/15 9:06
 */
public class MeasureAndFilterColumnVO {
    private String key;
    private List<String> values;

    public MeasureAndFilterColumnVO() {
    }

    public MeasureAndFilterColumnVO(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
