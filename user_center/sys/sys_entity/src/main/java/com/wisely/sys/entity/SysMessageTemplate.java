package com.wisely.sys.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息模板表(SysMessageTemplate)实体类
 *
 * @author makejava
 * @since 2022-10-31 15:29:37
 */
@Setter
@Getter
public class SysMessageTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 867272485422324640L;
    
    private Integer id;
    /**
    * 版本号
    */
    private Integer version;
    /**
    * 模板编码
    */
    private String code;
    /**
    * 模板名称
    */
    private String name;
    /**
    * 业务类型
    */
    private String biztype;
    /**
    * 消息类型
    */
    private String msgtype;
    /**
    * 标题
    */
    private String title;
    /**
    * 内容
    */
    private String content;
    /**
    * 是否启用 1-是 0-否
    */
    private Integer status;
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

    /** 扩展字段 **/
    private String idQueryIn;

}
