package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 角色(UcenterRole)实体类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:33:21
 */
@Setter
@Getter
public class UcenterRoleVo extends BaseEntity implements Serializable , EntityCacheManager {

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
}
