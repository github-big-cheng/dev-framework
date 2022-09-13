package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 机构信息详情表(UcenterOrgInfo)实体类
 *
 * @author system
 * @since 2022-09-07 14:21:27
 */
@Setter
@Getter
public class UcenterOrgInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 562121901535843221L;
    /**
     * id
     */
    private Integer id;
    /**
     * 机构表id
     */
    private Integer orgId;
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


    /* 扩展属性 */
    private String orgIdQueryIn;
}
