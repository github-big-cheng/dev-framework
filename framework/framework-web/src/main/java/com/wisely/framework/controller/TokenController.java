package com.wisely.framework.controller;

import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.lock.WiselyLock;
import com.wisely.framework.helper.lock.WiselyLockFactory;
import com.wisely.framework.plugins.token.TokenProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/token")
public class TokenController {

    public TokenController(WiselyLockFactory wiselyLockFactory, TokenProperties tokenProperties) {
        this.expiredTime = tokenProperties.getExpiredTime();
        this.wiselyLock = wiselyLockFactory.build(tokenProperties.getModel(), this.expiredTime);
    }


    WiselyLock wiselyLock;

    int expiredTime;


    @RequestMapping("/get")
    @ResponseBody
    public Object get() {

        String token = RandomHelper.uuid();
        wiselyLock.unlock(token); // 复用了 DoUnionLock, 感觉不是很好...
        boolean flag = wiselyLock.lock(RequestHelper.getSessionId(), token, this.expiredTime * 1000); // 默认5分钟
        if (!flag) {
            throw SystemException.builder("token build failed");
        }
        return ResponseBuilder.buildSuccess(token);
    }


}
