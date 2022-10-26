package com.wisely.sys.common;

import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.helper.ConfigHelper;
import com.wisely.framework.helper.SpringHelper;
import com.wisely.sys.common.cache.CodeCache;
import com.wisely.sys.common.cache.FunctionCache;
import com.wisely.sys.common.cache.ParameterCache;
import com.wisely.sys.handler.SysConverterDictionary;
import com.wisely.sys.service.SysCacheService;
import com.wisely.ucenter.client.handler.UcenterConverterDictionary;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class SysWebConfig {

    @Bean
    public ConverterDictionary ucenterDictionary() {
        return new UcenterConverterDictionary();
    }

    @Bean
    public ConverterDictionary sysDictionary() {
        return new SysConverterDictionary();
    }


    @Bean
    public CodeCache codeCache() {
        return new CodeCache();
    }

    @Bean
    public FunctionCache functionCache() {
        return new FunctionCache();
    }

    @Bean
    public ParameterCache parameterCache() {
        return new ParameterCache();
    }

    /**
     * Spring容器加载完毕，初始化相关缓存数据
     *
     * @return
     */
    @Bean
    public ApplicationListener<ContextRefreshedEvent> applicationListener() {
        return applicationEvent -> {
            if (ConfigHelper.getBoolean("sys.caches.enabled")) {
                SysCacheService sysCacheService = SpringHelper.getBean(SysCacheService.class);
                sysCacheService.refreshByType(null);
            }
        };
    }

}
