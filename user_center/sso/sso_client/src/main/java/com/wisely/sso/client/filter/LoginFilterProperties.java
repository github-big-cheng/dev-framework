package com.wisely.sso.client.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LoginFilterProperties {
    /**
     * 是否开启登录过滤器
     */
    private boolean enabled;

    /**
     * 过滤请求路径
     */
    private String urlPatterns = "/*";

    /**
     * 请求校验方式
     * local
     * remote
     */
    private String mode = "local";

    /**
     * 验证是否合法路径
     */
    private String checkLoginUrl;

    /**
     * 跳转路径
     */
    private String redirectUrl = "";

    /**
     * 优先级
     */
    private int order = 1;

    /**
     * 过期时间
     */
    private int expiredTime = 1000 * 60 * 10;

    /**
     * 初始化参数
     */
    private Map<String, String> initParameters;

}
