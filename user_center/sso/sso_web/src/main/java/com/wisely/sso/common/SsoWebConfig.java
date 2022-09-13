package com.wisely.sso.common;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.wisely.framework.cloud.handler.sentinel.SentinelRestTemplateHandler;
import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.plugins.net.RestTemplateNetTools;
import com.wisely.sso.client.handler.Base64DataConverterImpl;
import com.wisely.sso.client.handler.LocalLoginCheck;
import com.wisely.sys.handler.SysConverterDictionary;
import com.wisely.ucenter.client.api.UcenterNetApi;
import com.wisely.ucenter.client.handler.UcenterConverterDictionary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableAsync
public class SsoWebConfig implements AsyncConfigurer {


    @Value("${ucenter.server.url}")
    private String ucenterUrl;


    @Bean
    public Base64DataConverterImpl base64DataConverter() {
        return new Base64DataConverterImpl();
    }


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
        return new RestTemplateNetTools(ucenterUrl, restTemplate);
    }

    @Bean
    public UcenterNetApi ucenterNetApi(RestTemplateNetTools restTemplateNetTools) {
        return new UcenterNetApi(restTemplateNetTools);
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
    public LocalLoginCheck localLoginCheck() {
        return new LocalLoginCheck();
    }


    /**
     * ServerEndpointExporter 作用
     * <p>
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 容器启动后动作
     *
     * @return
     */
    @Bean
    public ApplicationListener<ContextRefreshedEvent> applicationListener() {
        return contextRefreshedEvent -> {

        };
    }
}
