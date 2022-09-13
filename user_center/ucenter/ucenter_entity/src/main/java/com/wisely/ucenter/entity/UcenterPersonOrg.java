package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 部门/机构人员中间表(UcenterPersonOrg)实体类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:33:07
 */
@Setter
@Getter
public class UcenterPersonOrg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -89307472854549665L;
    /**
     * ID ID
     */
    private Integer id;

    /**
     * 部门对应机构id
     */
    private Integer rootOrgId;
    /**
     * 部门/机构 id
     */
    private Integer deptId;
    /**
     * 人员
     */
    private Integer personId;
    /**
     * 是否主机构 1：是   0或null：否
     */
    private Integer isMain;
    /**
     * 是否是主要负责人  1:是  0:否
     */
    private Integer isMainPerson;
    /**
     * 职位id
     */
    private Integer posId;
    /**
     * 职级
     */
    private String posLevel;
    /**
     * 顺序号
     */
    private Integer orderNo;
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
     * 修改人
     */
    private Integer updateBy;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;



    /*------------扩展属性--------------------*/
    /**
     * 多个id
     */
    private String idQueryIn;
    /**
     * 批量用户id,逗号分隔
     */
    private String personIdQueryIn;
    /**
     * 批量用户id,逗号分隔
     */
    private String personIdQueryNotIn;
    /**
     * 部门（含子集）
     */
    private Integer deptIdQueryInWithChild;
    /**
     * 部门名称搜索
     */
    private String deptNameQueryLike;

    /**
     * 人员名称搜索
     */
    private String personNameQueryLike;

    /**
     * 机构id集合
     */
    private String rootOrgIdQueryIn;
    /**
     * 部门id集合，逗号分割
     */
    private String deptIdQueryIn;
}
