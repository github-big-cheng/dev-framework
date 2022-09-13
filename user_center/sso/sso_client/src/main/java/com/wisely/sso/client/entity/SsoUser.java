package com.wisely.sso.client.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Setter
@Getter
public class SsoUser {

    // 账户有关
    /**
     * 登录Token
     */
    private String ticket;
    /**
     * 用户ID
     * UcenterUser.id
     */
    private Integer id;
    /**
     * 账号
     * UcenterUser.account
     */
    private String account;

    // 人员基本信息
    /**
     * UcenterPerson.id
     */
    private Integer personId;
    /**
     * 人员姓名
     * UcenterPerson.personName
     */
    private String personName;
    /**
     * 身份证号
     * UcenterPerson.idNo
     */
    private String idNo;
    /**
     * 工号
     * UcenterPerson.jobNo
     */
    private String jobNo;
    /**
     * 性别
     * UcenterPerson.sex
     * UcenterCode TYPE=10014
     */
    private String sex;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 手机号
     * UcenterPerson.mobile
     */
    private String mobile;
    /**
     * 用户头像
     */
    private String imgPath;
    /**
     * 是否首次登录 1-是 0-否
     */
    private Integer firstLogin;

    // 组织机构
    /**
     * 主组织
     */
    private Integer orgId;
    /**
     * 主组织
     */
    private String orgName;
    /**
     * 多机构
     */
    private String organizations;

    /**
     * 主部门
     */
    private Integer mainDeptId;
    /**
     * 主部门
     */
    private String mainDeptName;
    /**
     * 用户部门（逗号分割）
     */
    private String departments;

    /**
     * 人员部门信息
     * 含职务、职级、是否主要部门、是否主要负责人等
     */
    private List<Model> deptInfo = Lists.newArrayList();

    // 角色
    /**
     * 用户角色Code
     * UcenterRole.code
     */
    private Set<String> roleCodes = Sets.newHashSet();

    // 权限
    /**
     * 用户权限
     * SysFunction.code
     */
    private Set<String> authCodes = Sets.newHashSet();

    /**
     * 扩展属性
     */
    private Model extendedProperties = Model.builder();
}
