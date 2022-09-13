package com.wisely.ucenter.common;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.wisely.framework.cloud.handler.sentinel.SentinelRestTemplateHandler;
import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.helper.ConfigHelper;
import com.wisely.framework.helper.SpringHelper;
import com.wisely.framework.plugins.net.RestTemplateNetTools;
import com.wisely.sso.client.handler.Base64DataConverterImpl;
import com.wisely.sys.api.SysNetApi;
import com.wisely.sys.handler.SysConverterDictionary;
import com.wisely.ucenter.caches.*;
import com.wisely.ucenter.client.handler.UcenterConverterDictionary;
import com.wisely.ucenter.service.UcenterCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UcenterWebConfig {

    @Value("${sys.server.url}")
    private String sysUrl;

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(
            blockHandler = SentinelRestTemplateHandler.SENTINEL_HANDLER_METHOD,
            blockHandlerClass = SentinelRestTemplateHandler.class,
            fallback = SentinelRestTemplateHandler.SENTINEL_FALLBACK_METHOD,
            fallbackClass = SentinelRestTemplateHandler.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplateNetTools restTemplateNetTools(RestTemplate restTemplate) {
        return new RestTemplateNetTools(sysUrl, restTemplate);
    }


    @Bean
    public SysNetApi sysNetApi(RestTemplateNetTools restTemplateNetTools) {
        return new SysNetApi(restTemplateNetTools);
    }

    @Bean
    public Base64DataConverterImpl base64DataConverter() {
        return new Base64DataConverterImpl();
    }

    @Bean
    public ConverterDictionary ucenterDictionary() {
        return new UcenterConverterDictionary();
    }

    @Bean
    public ConverterDictionary sysDictionary() {
        return new SysConverterDictionary();
    }

    @Bean
    public PersonCache personCache() {
        return new PersonCache();
    }

    @Bean
    public UserCache userCache() {
        return new UserCache();
    }

    @Bean
    public OrgCache orgCache() {
        return new OrgCache();
    }

    @Bean
    public RoleCache roleCache() {
        return new RoleCache();
    }

    @Bean
    public PositionCache positionCache() {
        return new PositionCache();
    }


    /**
     * Spring容器加载完毕，初始化相关缓存数据
     *
     * @return
     */
    @Bean
    public ApplicationListener<ContextRefreshedEvent> applicationListener() {
        return applicationEvent -> {
            // 加载缓存
            if (ConfigHelper.getBoolean("ucenter.caches.enabled")) {
                UcenterCacheService ucenterCacheService = SpringHelper.getBean(UcenterCacheService.class);
                ucenterCacheService.refreshByType(null);
            }
        };
    }
}
