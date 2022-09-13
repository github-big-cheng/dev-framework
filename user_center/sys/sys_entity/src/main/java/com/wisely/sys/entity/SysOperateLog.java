package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志表(SysOperateLog)实体类
 *
 * @author system
 * @since 2022-09-06 15:21:07
 */
@Setter
@Getter
public class SysOperateLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 888740492265891834L;
    /**
    * id
    */
    private Integer id;
    /**
    * 操作人
    */
    private String name;
    /**
    * IP地址
    */
    private String ip;
    /**
    * 机器名称
    */
    private String machineName;
    /**
    * 用户id
    */
    private Integer userId;
    /**
    * 操作时间
    */
    private String opTime;
    /**
    * 请求内容
    */
    private String requestMsg;
    /**
    * 相应内容
    */
    private String responseMsg;
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


    /* 扩展字段 */

    private String opTimeQueryGe;

    private String opTimeQueryLe;

    private String accountQueryLike;

}
