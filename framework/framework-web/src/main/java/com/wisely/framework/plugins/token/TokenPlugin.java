package com.wisely.framework.plugins.token;

import com.wisely.framework.controller.TokenController;
import com.wisely.framework.handler.TokenHandler;
import com.wisely.framework.helper.lock.LocalLock;
import com.wisely.framework.helper.lock.WiselyLockFactory;
import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * 表单防重复提交插件
 */
@ConditionalOnProperty(prefix = "plugins.token", value = "enabled", havingValue = "true")
public class TokenPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "TokenPlugin";
    }


    @Bean
    @ConfigurationProperties(prefix = "plugins.token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }

    @Bean
    public LocalLock localLock(TokenProperties tokenProperties) {
        return new LocalLock(tokenProperties.getExpiredTime());
    }

    @Bean
    public WiselyLockFactory lockFactory() {
        return new WiselyLockFactory();
    }

    @Bean
    public TokenHandler tokenHandler(WiselyLockFactory wiselyLockFactory, TokenProperties tokenProperties) {
        return new TokenHandler(wiselyLockFactory, tokenProperties);
    }

    @Bean
    public TokenController tokenController(WiselyLockFactory wiselyLockFactory, TokenProperties tokenProperties) {
        return new TokenController(wiselyLockFactory, tokenProperties);
    }

}
