package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UcenterUserVo extends BaseEntity implements Serializable, EntityCacheManager {

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
     * 错误次数
     */
    private Integer isErrorTime;
    /**
     * 账号状态 1- 正常   2-冻结(锁定)   3-注销
     */
    private Integer status;

}
