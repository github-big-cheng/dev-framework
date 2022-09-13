package com.wisely.sso.client.handler;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BusinessException;
import com.wisely.framework.exception.eum.ExceptionCodeEnum;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoFunction;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.helper.UserHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalLoginCheck implements LoginCheck, SsoConstants {

    @Override
    public SsoUser loginCheck(Model input) {

        String _sgk = input.getString(SSO_KEY);
        String osType = input.getString(OS_KEY, OS_PC);

        SsoUser ssoUser;
        try {
            ssoUser = LoginHandler.doCheck(_sgk, osType);
        } catch (BusinessException e) {
            log.error("loginCheck failed:{}", e);
            throw BusinessException.builder(e, ExceptionCodeEnum.USER_NOT_LOGIN_IN.getCode(), e.getMessage());
        }

        // 权限校验
        String action = input.getString("action");
        if (StringHelper.isNotBlank(action)) {
            // 系统管理员给予所有权限
            if (UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
                return ssoUser;
            }

            try {
                byte[] functionByte = RedisHelper.hgetBytes(SsoConstants.FUNCTION_KEY, action);
                AssertHelper.EX_SYSTEM.isNotEmpty(functionByte, "login.function_config_error.{0}", action);

                SsoFunction ssoFunction = ProtoBufHelper.deserializer(functionByte, SsoFunction.class);
                AssertHelper.EX_BUSINESS.isNotEmpty(ssoFunction, "login.function_config_error.{0}", action);
                boolean authFlag = ssoUser.getAuthCodes().contains(DataHelper.getString(ssoFunction.getCode()));
                AssertHelper.EX_BUSINESS.isTrue(authFlag, "login.account_is_not_auth");
            } catch (BusinessException e) {
                log.error("authCheck failed:{}", e);
                throw BusinessException.builder(e, ExceptionCodeEnum.BUSINESS.getCode(), e.getMessage());
            }
        }

        return ssoUser;
    }
}

