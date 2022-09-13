package com.wisely.framework.plugins.filter;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FrameworkFilterProperties {

    /**
     * 是否开启
     * 默认开启frameworkFilter
     */
    private boolean enable = true;

    /**
     * 过滤请求
     */
    private String[] patterns = {"/*"};

    /**
     * filter排序
     */
    private Integer order = 0;
}
