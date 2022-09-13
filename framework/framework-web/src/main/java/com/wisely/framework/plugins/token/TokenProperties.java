package com.wisely.framework.plugins.token;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenProperties {

    /**
     * 是否激活token插件
     */
    private boolean enabled;

    /**
     * token保存方式，默认local
     */
    private String model = "localLock"; // redis

    private String tokenKey = "_token"; // token key

    /**
     * token过期时间 单位秒
     */
    private int expiredTime = 5 * 60;
}
