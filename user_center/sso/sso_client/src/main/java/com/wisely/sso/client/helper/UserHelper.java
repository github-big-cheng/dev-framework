package com.wisely.sso.client.helper;

import com.google.common.collect.Sets;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.sso.client.entity.SsoUser;

import java.util.Set;

import static com.wisely.sso.client.SsoConstants.OS_PC;
import static com.wisely.sso.client.SsoConstants.TICKET_PREFIX;

public class UserHelper {


    /**
     * 线程变量
     */
    private final static ThreadLocal<SsoUser> THREAD_LOCAL_MAP = new ThreadLocal<>();


    public static void setUser(SsoUser user) {
        THREAD_LOCAL_MAP.set(user);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static SsoUser getUser() {
        return THREAD_LOCAL_MAP.get();
    }


    /**
     * 清理
     */
    public static void clear() {
        THREAD_LOCAL_MAP.remove();
    }


    /**
     * 获取userId
     *
     * @return
     */
    public static Integer getUserId() {
        return getUser() == null ? null : getUser().getId();
    }


    /**
     * 获取账号
     *
     * @return
     */
    public static String getAccount() {
        return getUser() == null ? null : getUser().getAccount();
    }

    /**
     * 获取登录票据
     *
     * @return
     */
    public static String getTicket() {
        return getUser() == null ? null : getUser().getTicket();
    }

    /**
     * 获取机构ID
     *
     * @return
     */
    public static Integer getOrgId() {
        return getUser() == null ? null : getUser().getOrgId();
    }

    /**
     * 获取机构名称
     *
     * @return
     */
    public static String getOrgName() {
        return getUser() == null ? null : getUser().getOrgName();
    }


    /**
     * 获取所有机构
     *
     * @return
     */
    public static String getOrganizations() {
        return getUser() == null ? null : getUser().getOrganizations();
    }

    /**
     * 获取主要部门
     */
    public static Integer getMainDeptId() {
        return getUser() == null ? null : getUser().getMainDeptId();
    }

    /**
     * 获取personId
     *
     * @return
     */
    public static Integer getPersonId() {
        return getUser() == null ? null : getUser().getPersonId();
    }

    /**
     * 获取personName
     *
     * @return
     */
    public static String getPersonName() {
        return getUser() == null ? null : getUser().getPersonName();
    }


    /**
     * 是否有指定权限代码
     *
     * @param authCodes
     * @return
     */
    public static boolean hasAuth(String... authCodes) {
        Set<String> authCodeSet = getUser() == null ? Sets.newHashSet() : getUser().getAuthCodes();
        return Sets.difference(Sets.newHashSet(authCodes), authCodeSet).size() == 0;
    }


    /**
     * 是否包含指定权限
     *
     * @param roleCodes
     * @return
     */
    public static boolean hasRole(String... roleCodes) {
        Set<String> roleCodeSet = getUser() == null ? Sets.newHashSet() : getUser().getRoleCodes();
        return Sets.difference(Sets.newHashSet(roleCodes), roleCodeSet).size() == 0;
    }


    /**
     * 根据票据获取用户
     *
     * @param osType
     * @param ticket
     * @return
     */
    public static SsoUser loadByTicket(String osType, String ticket) {
        String userJson = RedisHelper.get(TICKET_PREFIX + osType + ticket);
        return StringHelper.isBlank(userJson) ? null : JsonHelper.json2Obj(userJson, SsoUser.class);
    }


    /**
     * 根据票据获取用户
     * load by default os type PC
     * @param ticket
     * @return
     */
    public static SsoUser loadByTicket(String ticket) {
        return loadByTicket(OS_PC, ticket);
    }
}
