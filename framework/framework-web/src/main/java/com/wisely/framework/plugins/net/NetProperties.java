package com.wisely.framework.plugins.net;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NetProperties {

    /**
     * 是否开启远程工具
     */
    private boolean enabled;

    /**
     * 是否开启连接池
     */
    private boolean userPool = false;

    /**
     * 最大连接数，同一时间最大并发数
     */
    private int maxTotal = 400;

    /**
     * 单host（可以理解为单域名）最大并发数，在依赖的外部系统数量较少时，可以配置和maxTotal一致。
     */
    private int defaultMaxPerRoute = 200;

    /**
     * 连接超时时间 单位ms
     */
    private int connectTimeout = 20 * 1000;

    /**
     * 握手成功后，socket通信超时时间 单位ms
     */
    private int readTimeout = 65 * 1000;

    /**
     * 从连接池获取连接的超时时间，一般设置为较短 单位ms
     */
    private int connectRequestTimeout = 3 * 1000;
}
