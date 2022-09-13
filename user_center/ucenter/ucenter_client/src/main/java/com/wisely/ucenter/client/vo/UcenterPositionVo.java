package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UcenterPositionVo extends BaseEntity implements Serializable, EntityCacheManager {


    private static final long serialVersionUID = -9033486073770807526L;
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

}
