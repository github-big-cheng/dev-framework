package com.wisely.gateway.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GatewayLog {

    /**
     * 访问实例
     */
    private String targetServer;
    /**
     * 请求路径
     */
    private String requestPath;
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
    private String osType = "PC";
    /**
     * 令牌
     */
    private String token;
    /**
     * 请求体
     */
    private String requestBody;
    /**
     * 响应体
     */
    private String responseData;
    /**
     * 请求ip
     */
    private String ip;
    /**
     * 请求时间
     */
    private LocalDateTime requestTime;
    /**
     * 响应时间
     */
    private LocalDateTime responseTime;
    /**
     * 执行时间
     */
    private long executeTime;

}
