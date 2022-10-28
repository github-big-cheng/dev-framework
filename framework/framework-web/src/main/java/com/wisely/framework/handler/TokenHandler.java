package com.wisely.framework.handler;

import com.wisely.framework.handler.annotation.Token;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.lock.WiselyLock;
import com.wisely.framework.helper.lock.WiselyLockFactory;
import com.wisely.framework.plugins.token.TokenProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 防重复提交处理器
 */
@Aspect
public class TokenHandler {

    @Autowired
    public TokenHandler(WiselyLockFactory wiselyLockFactory, TokenProperties tokenProperties) {
        this.wiselyLock = wiselyLockFactory.build(tokenProperties.getModel(), tokenProperties.getExpiredTime());
        this.tokenProperties = tokenProperties;
    }

    WiselyLock wiselyLock;

    TokenProperties tokenProperties;


    @Around(value = "@annotation(token)")
    public Object around(ProceedingJoinPoint joinPoint, Token token) throws Throwable {

        //TODO 分布式场景这里会有问题，暂不处理
        String key = RequestHelper.getSessionId();
        AssertHelper.EX_VALIDATION.isNotBlank(key, "token.current_session_is_invalided");

        // token校验
        checkRepeat(RequestHelper.getRequest().getParameter(tokenProperties.getTokenKey()), key);

        Object result = joinPoint.proceed();

        wiselyLock.unlock(key);

        return result;
    }


    /**
     * token校验
     *
     * @param requestToken
     * @param key
     * @return
     */
    private boolean checkRepeat(String requestToken, String key) {
        AssertHelper.EX_VALIDATION.isNotBlank(requestToken, "common.parameter_required.token");
        String serviceToken = wiselyLock.getValue(key);
        AssertHelper.EX_VALIDATION.isNotBlank(serviceToken, "token.do_token_request_first");
        AssertHelper.EX_VALIDATION.isEquals(serviceToken, requestToken, "token.repeat_request");
        return true;
    }

}
