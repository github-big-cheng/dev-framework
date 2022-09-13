package com.wisely.ucenter.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 人员信息详情表(UcenterPersonInfo)实体类
 *
 * @author system
 * @since 2022-09-07 11:41:55
 */
@Setter
@Getter
public class UcenterPersonInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 529913741801223219L;
    /**
    * id
    */
    private Integer id;
    /**
    * 人员id
    */
    private Integer personId;
    /**
     * key
     */
    private String fieldKey;
    /**
     * 值
     */
    private String fieldValue;
    /**
    * 操作状态
    */
    private Integer opState;
    /**
    * 创建人
    */
    private Integer createBy;
    /**
    * 创建时间
    */
    private String createTime;
    /**
    * 修改人
    */
    private Integer updateBy;
    /**
    * 修改时间
    */
    private String updateTime;
    /**
    * 是否删除 0-否 1-是
    */
    private Integer isDeleted;


    /* 扩展字段 */
    private String personIdQueryIn;
}
