package com.wisely.framework.plugins.datasource.hikari;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HikariProperties extends HikariConfig {

    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 数据库驱动
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    /**
     * 最小连接数
     */
    private volatile int minIdle = 5;
    /**
     * 最大连接数
     */
    private volatile int maxPoolSize = 20;
    /**
     * 连接测试语句
     */
    private String connectionTestQuery = "SELECT 1";
}
