package com.rigai.rigeye.common.bean;

import com.rigai.rigeye.common.constant.CommonConstant;
import com.rigai.rigeye.common.constant.ComparisonOperator;
import com.rigai.rigeye.common.constant.ValueTypeDescription;
import com.rigai.rigeye.common.model.AlertRule;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/21.
 */

public class AlertRuleConstruct {
    /**
     * 报警渠道
     * 0代表不发送，1代表短信，2代表微信，3代表邮件
     */
    @NotNull
    @Size(min = 1)
    private List<String> channel;

    /**
     * 时间，单位分
     */
    @NotNull
    @Min(1L)
    private Long period;

    /**
     * 对应的 数据集变量
     */
    @NotNull
    private String variable;

    @NotNull
    private String norm;
    /**
     * 取值类型，总和，最大值，最小值,平均值
     */
    @NotNull
    @Min(1)
    @Max(4)
    private Integer valueType;

    /**
     * 比较符
     */
    @NotNull
    @Min(1)
    @Max(4)
    private Integer comparisonOperators;

    /**
     * 阈值
     */
    private Long threshold;

    private Long ruleId;

    public AlertRuleConstruct() {
    }

    public AlertRuleConstruct(AlertRule rule) {
        this.comparisonOperators=rule.getComparisonOperators();
        this.period=rule.getPeriod();
        this.channel = Arrays.asList(rule.getChannel().split(CommonConstant.SPLIT_SIGN));
        this.valueType=rule.getValueType();
        this.threshold=rule.getThreshold();
        this.variable=rule.getVariable();
        this.norm =rule.getNorm();
        this.ruleId=rule.getId();
    }

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public Integer getComparisonOperators() {
        return comparisonOperators;
    }

    public void setComparisonOperators(Integer comparisonOperators) {
        this.comparisonOperators = comparisonOperators;
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getNorm() {
        return norm;
    }

    public void setNorm(String norm) {
        this.norm = norm;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public String toString() {
        return "最近 "+period+" 分钟 "+variable+" "+ norm +" "+ValueTypeDescription.getValueTypeDescription(valueType)
                + ComparisonOperator.getComparisonOperator(comparisonOperators)+threshold;
    }

    public AlertRule toAlertRule(){
        AlertRule rule=new AlertRule();
        rule.setValueType(this.getValueType());
        rule.setVariable(this.getVariable());
        rule.setThreshold(this.getThreshold());
        List<String> channels=this.getChannel();
        StringBuilder channelStr=new StringBuilder();
        for(int i=0;i<channels.size();i++){
            channelStr.append(channel.get(i));
            if(i!=(channels.size()-1)){
                channelStr.append(CommonConstant.SPLIT_SIGN);
            }
        }
        rule.setChannel(channelStr.toString());
        rule.setPeriod(this.period);
        rule.setNorm(this.norm);
        rule.setComparisonOperators(this.comparisonOperators);
        if(this.ruleId!=null){
            rule.setId(this.ruleId);
        }
        return rule;
    }
}
