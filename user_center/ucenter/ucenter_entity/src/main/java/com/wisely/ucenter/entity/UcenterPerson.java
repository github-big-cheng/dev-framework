package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 人员(UcenterPerson)实体类
 *
 * @author system
 * @since 2021-07-26 09:28:23
 */
@Setter
@Getter
public class UcenterPerson extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 506133566246685605L;
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
     * 姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 工号
     */
    private String jobNo;
    /**
     * 性别
     */
    private String sex;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 电话
     */
    private String telephone;
    /**
     * email
     */
    private String email;
    /**
     * 婚姻状况
     */
    private String marriage;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 国籍
     */
    private String nation;
    /**
     * 员工状态
     */
    private String status;
    /**
     * 顺序号
     */
    private Integer orderNo;
    /**
     * 备注
     */
    private String memo;
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


    //扩展字段
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 账号状态 1- 正常   2-冻结(锁定)   3-注销
     */
    private Integer accountStatus;
    /**
     * 人员账号
     */
    private String account;


    /**
     * 批量id,逗号分隔
     */
    private String idQueryIn;
    /**
     * 批量orgId
     */
    private String orgIdQueryIn;
    /**
     * 姓名模糊查询
     */
    private String nameQueryLike;
    /**
     * 部门id精确查询
     */
    private Integer deptIdQueryEq;
    /**
     * 机构下部门查询
     */
    private String deptIdQueryIn;
    /**
     * 角色id精确查询
     */
    private Integer roleIdQueryEq;
    /**
     * 角色代码精确查询
     */
    private String roleCodeQueryEq;
    /**
     * 姓名或者账号模糊查询
     */
    private String nameOrAccountQueryLike;
    /**
     * 账号精确查找
     */
    private String accountQueryEq;
}
