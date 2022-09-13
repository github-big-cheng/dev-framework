package com.wisely.sso.service;

import com.wisely.framework.entity.Model;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.entity.UcenterUser;

import java.util.List;

public interface LoginService {


    /**
     * 用户列表查询
     *
     * @param query
     * @return
     */
    List<UcenterUser> findUserByEntity(UcenterUser query);

    /**
     * 根据账号获取用户对象
     *
     * @param account
     * @return
     */
    UcenterUser findUserByAccount(String account);


    void doLogin(UcenterUser user, boolean success);

    /**
     * 保存登录日志
     *
     * @param ssoUser
     * @param osType
     * @param account
     * @param opStat
     */
    void loginLogSave(SsoUser ssoUser, String osType, String account, int opStat);

    /**
     * 跳转登录
     *
     * @return
     */
    SsoUser redirectLogin();


    /**
     * 登录、权限校验
     *
     * @param input
     * @return
     */
    SsoUser doCheck(Model input);
}
