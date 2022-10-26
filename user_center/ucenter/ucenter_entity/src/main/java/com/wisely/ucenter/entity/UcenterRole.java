package com.wisely.ucenter.entity;

import java.io.Serializable;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色(UcenterRole)实体类
 *
 * @author system
 * @since 2021-05-28 17:33:21
 */
@Setter
@Getter
public class UcenterRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -93083949006181734L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 机构ID 为系统后续多机构新增字段
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 顺序号
     */
    private Integer orderNo;
    /**
     * 是否是系统初始化 0否1是
     */
    private Integer isSys;
    /**
     * 备注
     */
    private String memo;
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

    //扩展字段
    /**
     * 查询参数:模糊分页查询代码/名称关键字
     */
    private String codeOrNameLike;
    /**
     * 名称模糊查询
     */
    private String nameQueryLike;
    /**
     * 代码模糊查询
     */
    private String codeQueryLike;
    /**
     * 批量id
     */
    private String idQueryIn;
    /**
     * orgId集合
     */
    private String orgIdQueryIn;
}
