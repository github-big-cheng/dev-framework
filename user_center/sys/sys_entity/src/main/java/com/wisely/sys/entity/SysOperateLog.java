package com.wisely.sys.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 操作日志表(SysOperateLog)实体类
 *
 * @author system
 * @since 2022-09-29 16:28:37
 */
@Setter
@Getter
public class SysOperateLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 658225419556219956L;
    /**
     * id
     */
    private Integer id;
    /**
     * 访问实例
     */
    private String targetServer;
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 请求ip
     */
    private String ip;
    /**
     * 请求方法
     */
    private String requestMethod;
    /**
     * 协议
     */
    private String schema;
    /**
     * 操作系统类型
     */
    private String osType;
    /**
     * 令牌
     */
    private String token;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 请求时间
     */
    private String requestTime;
    /**
     * 响应时间
     */
    private String responseTime;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 响应体
     */
    private String responseData;
    /**
     * 执行时间
     */
    private Integer executeTime;
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

    private String targetServerQueryLike;

    private String requestTimeQueryGe;

    private String requestTimeQueryLe;


}
