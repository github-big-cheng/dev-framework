package com.wisely.sso.client.handler;

import com.wisely.framework.entity.Model;
import com.wisely.sso.client.entity.SsoUser;

public interface LoginCheck {

    /**
     * 登录、权限校验
     *
     * @param params
     * @return
     */
    SsoUser loginCheck(Model params);
}
