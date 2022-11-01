package com.wisely.sys.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息记录表(SysMessage)实体类
 *
 * @author makejava
 * @since 2022-10-31 15:30:02
 */
@Setter
@Getter
public class SysMessage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -67552298215952264L;
    
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
    * 业务类型
    */
    private String biztype;
    /**
    * 消息类型: 通知、站内信、邮件、短信、大屏、广播等
    */
    private String msgType;
    /**
    * 消息模板ID
    */
    private Integer templateId;
    /**
    * 标题
    */
    private String title;
    /**
    * 内容
    */
    private String content;
    /**
    * 消息参数
    */
    private String params;
    /**
    * 发送人ID
    */
    private String senderid;
    /**
    * 发送人
    */
    private String sender;
    /**
    * 发送状态: 0-待发送 1-已发送
    */
    private Integer sendStatus;
    /**
    * 发送时间
    */
    private String sendTime;
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

    private String idQueryNotIn;

    private String bizTypeQueryIn;

    private String bizTypeQueryNotIn;

    private String msgTypeQueryIn;

    private String msgTypeQueryNotIn;

    private String templateIdQueryIn;

    private String templateIdQueryNotIn;

    private String senderIdQueryIn;

    private String senderIdQueryNotIn;

}
