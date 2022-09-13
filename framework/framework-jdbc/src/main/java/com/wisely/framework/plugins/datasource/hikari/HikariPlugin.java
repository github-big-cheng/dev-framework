package com.wisely.framework.plugins.datasource.hikari;

import com.wisely.framework.plugins.AbstractPlugin;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@ConditionalOnProperty(prefix = "plugins.hikari", name = "enabled", havingValue = "true")
public class HikariPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "HikariPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.hikari")
    public HikariProperties hikariProperties() {
        return new HikariProperties();
    }

    @ConditionalOnMissingBean
    @Bean
    @Primary
    public DataSource dataSource(HikariProperties hikariProperties) {
        HikariDataSource datasource = new HikariDataSource(hikariProperties);
        return datasource;
    }
}
