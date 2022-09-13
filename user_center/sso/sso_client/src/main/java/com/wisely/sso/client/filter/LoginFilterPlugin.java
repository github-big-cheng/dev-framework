package com.wisely.sso.client.filter;

import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import com.wisely.sso.client.handler.LocalLoginCheck;
import com.wisely.sso.client.handler.LoginCheck;
import com.wisely.sso.client.handler.RemoteLoginCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 过滤器插件
 */
@Configuration
@ConditionalOnProperty(prefix = "plugins.filter.sso", name = "enabled", havingValue = "true")
@Slf4j
public class LoginFilterPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "LoginFilterPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.filter.sso")
    public LoginFilterProperties filterProperties() {
        return new LoginFilterProperties();
    }

    @Bean
    public LoginFilter loginFilter() {
        return new LoginFilter();
    }

    @Bean
    public FilterRegistrationBean myFilterBean(LoginFilterProperties loginFilterProperties, LoginFilter filter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);//设置为自定义的过滤器MyFilter
        filterRegistrationBean.addUrlPatterns(loginFilterProperties.getUrlPatterns());//拦截所有请求
        filterRegistrationBean.setOrder(loginFilterProperties.getOrder());//优先级为
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(LoginCheck.class)
    public LoginCheck loginCheck(LoginFilterProperties loginFilterProperties) {
        if (StringHelper.equalsIgnoreCase(loginFilterProperties.getMode(), "remote")) {
            return new RemoteLoginCheck();
        }
        return new LocalLoginCheck();
    }
}
