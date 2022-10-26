package com.wisely.framework.plugins.converter;

import com.wisely.framework.handler.aop.ConverterHandler;
import com.wisely.framework.handler.aop.DefaultConverterHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(prefix = "plugins.converter", value = "enabled", havingValue = "true")
@Import(ConverterPlugin.class)
public class ConverterWebPlugin {

    /**
     * 开启 @Converter 注解
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ConverterHandler.class)
    public DefaultConverterHandler converterHandler() {
        return new DefaultConverterHandler();
    }
}
