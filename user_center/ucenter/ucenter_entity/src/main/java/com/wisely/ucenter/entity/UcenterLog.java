package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 登录日志(UcenterLog)实体类
 *
 * @author system
 * @since 2021-05-28 17:32:17
 */
@Setter
@Getter
public class UcenterLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -14460286733962860L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 机构ID
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 操作时间
     */
    private String loginTime;
    /**
     * 操作类型 T_SYS_CODE10078-10:登录  10078-20 登出
     */
    private String type;
    /**
     * 事件内容
     */
    private String opContent;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 计算机名
     */
    private String machineName;
    /**
     * 用户ID T_UCENTER_USER
     */
    private Integer userId;
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
     * 查询开始时间
     */
    private String timeQueryGe;

    /**
     * 查询结束时间
     */
    private String timeQueryLe;

    /**
     * 操作人模糊查询关键字
     */
    private String accountQueryLike;
    /**
     * orgId集合
     */
    private String orgIdQueryIn;

}
