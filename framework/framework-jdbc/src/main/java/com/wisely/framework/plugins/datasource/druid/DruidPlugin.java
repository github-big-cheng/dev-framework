package com.wisely.framework.plugins.datasource.druid;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * druid数据源插件
 */
@ConditionalOnProperty(prefix = "plugins.druid", name = "enabled", havingValue = "true")
@Slf4j
public class DruidPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "DruidPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.druid")
    public DruidProperties druidProperties() {
        return new DruidProperties();
    }


    @ConditionalOnMissingBean
    @Bean
    @Primary
    public DataSource dataSource(DruidProperties druidProperties) {

        DruidDataSource datasource = new DruidDataSource();

        // basic
        datasource.setUrl(druidProperties.getUrl());
        datasource.setUsername(druidProperties.getUsername());
        datasource.setPassword(druidProperties.getPassword());
        datasource.setDriverClassName(druidProperties.getDriverClassName());

        // other config
        datasource.setInitialSize(druidProperties.getInitialSize());
        datasource.setMinIdle(druidProperties.getMinIdle());
        datasource.setMaxActive(druidProperties.getMaxActive());
        datasource.setMaxWait(druidProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(druidProperties.getValidationQuery());
        datasource.setTestWhileIdle(druidProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(druidProperties.getTestOnBorrow());
        datasource.setTestOnReturn(druidProperties.getTestOnReturn());
        datasource.setPoolPreparedStatements(druidProperties.getPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            String filters = druidProperties.getFilters();
            if (druidProperties.isControlEnabled() &&
                    !StringHelper.contains(filters, "stat")) {
                filters = String.join(",", filters, "stat");
            }
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter : {0}", e);
        }
        datasource.setConnectionProperties(druidProperties.getConnectionProperties());

        return datasource;
    }

    @Bean
    @ConditionalOnExpression("'true'.equals('${plugins.druid.control-enabled}')")
    public ServletRegistrationBean druidServlet(DruidProperties druidProperties) {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        bean.addInitParameter("resetEnable", druidProperties.getControlResetEnable());
        bean.addInitParameter("loginUsername", druidProperties.getControlLoginUsername());
        bean.addInitParameter("loginPassword", druidProperties.getControlLoginPassword());
        return bean;
    }

    @Bean
    @ConditionalOnExpression("'true'.equals('${plugins.druid.control-enabled}')")
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("exclusions", "*.js,*.gif,/druid/*");
        return bean;
    }


    @Bean
    public StatFilter statFilter(DruidProperties druidProperties) {
        StatFilter statFilter = new StatFilter();
        statFilter.setMergeSql(druidProperties.isMergeSql());
        return new StatFilter();
    }


}
