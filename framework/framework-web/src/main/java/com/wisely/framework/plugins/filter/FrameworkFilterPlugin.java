package com.wisely.framework.plugins.filter;

import com.wisely.framework.handler.FrameworkFilter;
import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

/**
 * FrameworkFilter插件
 * 将单次读取的request.getInputStream转换为可重复读的字节流
 * <p>
 * matchIfMissing = true
 * 默认加载
 * 如需关闭，请配置 plugins.filter.framework.enable=true
 */
@ConditionalOnProperty(prefix = "plugins.filter.framework", value = "enabled", havingValue = "true", matchIfMissing = true)
public class FrameworkFilterPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "FrameworkFilterPlugin";
    }

    @Bean
    public FrameworkFilter frameworkFilter() {
        return new FrameworkFilter();
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.filer.framework")
    public FrameworkFilterProperties frameworkFilterProperties() {
        return new FrameworkFilterProperties();
    }

    @Bean
    public FilterRegistrationBean frameworkFilterBean(FrameworkFilterProperties frameworkFilterProperties, FrameworkFilter filter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);//设置为自定义的过滤器MyFilter
        filterRegistrationBean.addUrlPatterns(frameworkFilterProperties.getPatterns());//拦截所有请求
        filterRegistrationBean.setOrder(frameworkFilterProperties.getOrder());//优先级为0
        return filterRegistrationBean;
    }
}
