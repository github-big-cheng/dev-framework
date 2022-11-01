package com.wisely.sys.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息接收表(SysMessageReceive)实体类
 *
 * @author makejava
 * @since 2022-10-31 15:29:51
 */
@Setter
@Getter
public class SysMessageReceive extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 840693268761366711L;
    
    private Integer id;
    /**
    * 消息id
    */
    private Integer messageId;
    /**
    * 接收人ID
    */
    private String receiverid;
    /**
    * 接收人
    */
    private String receiver;
    /**
    * 接收状态: 0-未读 1-已读
    */
    private Integer receiveStatus;
    /**
    * 接收时间
    */
    private String receiveTime;
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
