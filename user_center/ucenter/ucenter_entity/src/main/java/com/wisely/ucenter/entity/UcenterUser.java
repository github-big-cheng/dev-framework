package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户(UcenterUser)实体类
 *
 * @author ruijie.hu
 * @since 2021-06-01 16:44:49
 */
@Setter
@Getter
public class UcenterUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 797101398247541444L;
    /**
     * ID
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
     * 人员
     */
    private Integer personId;
    /**
     * 登录帐号 全局唯一（登录账号必须手机号）
     */
    private String account;
    /**
     * 登录密码 采用MD5加密
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 错误次数
     */
    private Integer isErrorTime;
    /**
     * 账号状态 1- 正常   2-冻结(锁定)   3-注销
     */
    private Integer status;
    /**
     * 是否首次登录 1-是 0-否
     */
    private Integer firstLogin;
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


    /* 扩展字段 */
    /**
     * 多id
     */
    private String idQueryIn;
    /**
     * 多个personId 逗号分隔
     */
    private String personIdQueryIn;

    /**
     * 多个账号  逗号分隔
     */
    private String accountQueryIn;

}
