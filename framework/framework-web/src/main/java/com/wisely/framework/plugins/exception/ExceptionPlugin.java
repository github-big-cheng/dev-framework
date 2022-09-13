package com.wisely.framework.plugins.exception;

import com.wisely.framework.handler.GlobalExceptionHandler;
import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 异常处理插件
 */
@ConditionalOnProperty(prefix = "plugins.exception", value = "enabled", havingValue = "true")
public class ExceptionPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "ExceptionPlugin";
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
