package com.wisely.sys.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SysCodeVo extends BaseEntity implements Serializable, EntityCacheManager {

    private static final long serialVersionUID = 509299191641337044L;
    /**
     * id
     */
    private Integer id;
    /**
     * id
     */
    private String value;
    /**
     * 类型
     */
    private String type;
    /**
     * 本地化
     */
    private String locale;
    /**
     * 路径
     */
    private String pathValue;
    /**
     * 代码值
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 父级
     */
    private String parent;
    /**
     * 代码类型 1-系统 0-用户
     */
    private Integer isSys;
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
}
