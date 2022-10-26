package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UcenterPersonVo extends BaseEntity implements Serializable, EntityCacheManager {

    private static final long serialVersionUID = -54739022558305992L;
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
     * 扩展信息
     */
    private Model info = Model.builder();

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
}
