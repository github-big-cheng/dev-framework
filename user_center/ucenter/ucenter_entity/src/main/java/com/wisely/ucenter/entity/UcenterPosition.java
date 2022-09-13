package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 职务表(UcenterPosition)实体类
 *
 * @author system
 * @since 2021-07-26 09:28:27
 */
@Setter
@Getter
public class UcenterPosition extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 229010855063840190L;
    /**
     * ID
     */
    private Integer id;
    /**
     * 公司id T_UCENTER_ORG
     */
    private Integer orgId;
    /**
     * 公司名称
     */
    private String orgName;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 职位类型 TYPE=12006
     */
    private String type;
    /**
     * 备注 MEMO
     */
    private String memo;
    /**
     * 操作状态
     */
    private Integer opState;
    /**
     * 创建人 T_UCENTER_USER
     */
    private Integer createBy;
    /**
     * 创建时间 yyyy-mm-dd hh:mm:ss
     */
    private String createTime;
    /**
     * 修改人 T_UCENTER_USER
     */
    private Integer updateBy;
    /**
     * 修改时间 yyyy-mm-dd hh:mm:ss
     */
    private String updateTime;
    /**
     * 是否删除 1-是 0-否
     */
    private Integer isDeleted;


    /* 扩展字段 */
    /**
     * 多ID
     */
    private String idQueryIn;
    /**
     * 代码、名称模糊查询关键字
     */
    private String nameOrCodeQueryLike;
    /**
     * 名称模糊搜索关键字
     */
    private String nameQueryLike;
    /**
     * 代码模糊搜索关键字
     */
    private String codeQueryLike;
    /**
     * orgId集合
     */
    private String orgIdQueryIn;
}
