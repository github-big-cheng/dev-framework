package com.wisely.sys.vo;

import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SysParameterVo implements Serializable, EntityCacheManager {

    private static final long serialVersionUID = -5684416574447393493L;

    /**
     * id
     */
    private Integer id;
    /**
     * 机构id
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * key
     */
    private String keies;
    /**
     * value
     */
    private String value;
    /**
     * 描述
     */
    private String description;
    /**
     * 参数类型
     */
    private String type;
    /**
     * 参数子类型
     */
    private String subType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer orderNo;

}
