package com.wisely.gateway.common;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class GateWayLogProperties {

    /**
     * 排序号
     */
    private Integer orderNo = -999;

    /**
     * 主题
     */
    private String topic = "sysLog";

    /**
     * 需记录日志的服务实例名称
     */
    private Set<String> serverNames = Sets.newHashSet();

}
