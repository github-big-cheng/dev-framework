package com.wisely.sso.client;

import com.wisely.framework.helper.ConfigHelper;

/**
 * 常量类
 */
public interface SsoConstants {

    /**
     * 管理员角色code
     */
    String ROLE_SUPER_ADMIN = "admin";

    /**
     * 登录用户默认有效期
     */
    Integer LOGIN_EXPIRATION_TIME = ConfigHelper.getInt("login.expiration-time", 10 * 24 * 60 * 60);

    // sso 相关
    /**
     * sso key
     */
    String SSO_KEY = "_sgk";

    /**
     * Ticket 前缀
     */
    String TICKET_PREFIX = "DOUNION_TICKET:";

    /**
     * 账号前缀
     */
    String ACCOUNT_PREFIX = "DOUNION_ACCOUNT:";

    /**
     * 图片验证码前缀
     */
    String VERIFY_IMAGE_PREFIX = "VERIFY_IMAGE:";
    /**
     * REDIRECT_LOGIN_CACHE
     */
    String REDIRECT_LOGIN_CACHE = "REDIRECT_LOGIN_CACHE:";


    /**
     * osType key
     */
    String OS_KEY = "osType";
    /**
     * 操作系统类型
     * - personal computer
     */
    String OS_PC = "PC";
    /**
     * 操作系统类型
     * - 安卓
     */
    String OS_ANDROID = "ANDROID";


    // 权限相关
    /**
     * 权限-缓存key前缀
     */
    String FUNCTION_KEY = "FUNCTION_HASH";


}
