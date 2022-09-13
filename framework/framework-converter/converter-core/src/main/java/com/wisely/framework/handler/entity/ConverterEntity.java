package com.wisely.framework.handler.entity;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.JsonHelper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
public class ConverterEntity implements Serializable {

    private static final long serialVersionUID = -3479728250183663065L;

    /**
     * 报文定义
     */
    private String name;
    /**
     * 报文描述
     */
    private String desc;
    /**
     * 输入
     */
    private List<ConverterItemEntity> send;
    /**
     * 输出
     */
    private List<ConverterItemEntity> rcv;



    // ========================================== 扩展属性 =================================================

    /**
     * 扩展属性
     */
    private Model extendField = Model.builder(); // 扩展属性


    public void setExtendField(String key, String val) {
        extendField.set(key, val);
    }


    public String getExtendField(String key) {
        return this.extendField.getString(key, null);
    }


    @Override
    public String toString() {
        return JsonHelper.obj2Json(this);
    }

}
