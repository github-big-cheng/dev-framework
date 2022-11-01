package com.wisely.gateway.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GateWayThreadPoolProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 10;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 20;

    /**
     * 队列长度
     */
    private int queueCapacity = 200;

    /**
     * 线程存活时间
     */
    private int keepAliveSeconds = 60;

    /**
     * 线程名称
     */
    private String threadNamePrefix = "taskExecutor-";

}
