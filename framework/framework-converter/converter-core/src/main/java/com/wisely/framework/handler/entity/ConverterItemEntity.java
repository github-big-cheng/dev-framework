package com.wisely.framework.handler.entity;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ConverterItemEntity implements Serializable {

    private static final long serialVersionUID = 6077292259186923053L;

    /**
     * 索引
     */
    private Integer index;
    /**
     * 字段名
     */
    private String name;
    /**
     * 类型
     */
    private String type = ConverterConstants.DATA_TYPE_STRING;
    /**
     * 描述
     */
    private String desc;
    /**
     * 备注
     */
    private String notes;

    /**
     * 循环体
     */
    private List<ConverterItemEntity> children;
    /**
     * 扩展属性
     */
    private Model extendField = Model.builder();


    public String getExtendField(String key) {
        return extendField.getString(key);
    }

    public void setExtendField(String key, String val) {
        extendField.set(key, val);
    }


    /**
     * 获取取值key
     *
     * @return
     */
    public String getKey() {
        return ValidHelper.isNotBlank(this.getTarget()) ? this.getTarget() : this.getName();
    }

    /**
     * 必输校验
     */
    public void validRequired() {
        AssertHelper.EX_VALIDATION.isTrue(!this.getRequired(), "common.parameter_required.{0}", this.getName());
    }

    // =================================    其他扩展字段     ==================================

    /**
     * 是否必输
     */
    private Boolean required = false;
    /**
     * 最大长度
     */
    private Integer maxlength;

    /**
     * 取值映射
     */
    private String target;
    /**
     * 默认值
     */
    private String defaultVal;
    /**
     * 精度
     */
    private Integer scale;
    /**
     * 需转换的map key
     */
    private String mapper;
    /**
     * 左截取长度
     */
    private Integer lsub;
    /**
     * 右截取长度
     */
    private Integer rsub;
    /**
     * 左补字符串
     */
    private String lpad;
    /**
     * 右补字符串
     */
    private String rpad;
    /**
     * 高优先级，为空时取maxlength
     */
    private Integer padLen;
    /**
     * 数据格式
     */
    private String fromPattern;
    /**
     * 目标格式
     */
    private String toPattern;

    @Override
    public String toString() {
        return JsonHelper.obj2Json(this);
    }

}
