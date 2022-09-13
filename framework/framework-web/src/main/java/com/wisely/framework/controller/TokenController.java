package com.wisely.framework.controller;

import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.lock.DoUnionLock;
import com.wisely.framework.helper.lock.DoUnionLockFactory;
import com.wisely.framework.plugins.token.TokenProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/token")
public class TokenController {

    public TokenController(DoUnionLockFactory doUnionLockFactory, TokenProperties tokenProperties) {
        this.expiredTime = tokenProperties.getExpiredTime();
        this.doUnionLock = doUnionLockFactory.build(tokenProperties.getModel(), this.expiredTime);
    }


    DoUnionLock doUnionLock;

    int expiredTime;


    @RequestMapping("/get")
    @ResponseBody
    public Object get() {

        String token = RandomHelper.uuid();
        doUnionLock.unlock(token); // 复用了 DoUnionLock, 感觉不是很好...
        boolean flag = doUnionLock.lock(RequestHelper.getSessionId(), token, this.expiredTime * 1000); // 默认5分钟
        if (!flag) {
            throw SystemException.builder("token build failed");
        }
        return ResponseBuilder.buildSuccess(token);
    }


}
