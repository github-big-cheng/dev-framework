package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 对象权限(UcenterObjFunc)实体类
 *
 * @author xi.wang
 * @since 2021-07-23 13:23:02
 */
@Setter
@Getter
public class UcenterObjFunc extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -75309176362741092L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 机构ID 法人
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 部门，角色，用户类型
     */
    private String objType;
    /**
     * 类型ID
     */
    private Integer objId;
    /**
     * 应用ID
     */
    private Integer projectId;
    /**
     * 功能 FUNC_ID
     */
    private Integer funcId;
    /**
     * 操作状态
     */
    private Integer opState;
    /**
     * 录入人
     */
    private Integer createBy;
    /**
     * 录入时间
     */
    private String createTime;
    /**
     * 更新人
     */
    private Integer updateBy;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;


    // 扩展字段
    /**
     * 功能 FUNC_ID 集合，逗号分割
     */
    private String funcIdQueryIn;
}
