package com.wisely.framework.plugins.datasource.druid;

import lombok.Getter;
import lombok.Setter;

/**
 * Druid插件属性配置
 */
@Setter
@Getter
public class DruidProperties {

    /**
     * 是否启用druid插件
     */
    private boolean enabled;

    /**
     * 数据库链接
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库驱动
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 初始化大小
     */
    private int initialSize = 5;

    /**
     * 最小链接数
     */
    private int minIdle = 5;

    /**
     * 最大活动数
     */
    private int maxActive = 20;

    /**
     * 配置获取连接等待超时的时间
     */
    private Long maxWait = 60000l;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private Long timeBetweenEvictionRunsMillis = 60000l;

    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private Long minEvictableIdleTimeMillis = 300000l;
    private String validationQuery = "SELECT 1 FROM DUAL";
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;

    /**
     * 打开PSCache，并且指定每个连接上PSCache的大小
     */
    private Boolean poolPreparedStatements = true;
    private int maxPoolPreparedStatementPerConnectionSize = 20;

    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
     */
    private String filters;
    /**
     * mergeSql
     */
    private boolean mergeSql = true;

    /**
     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
     */
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";


    /**
     * 是否启用控制台
     */
    private boolean controlEnabled = false;
    private String controlLoginUsername = "admin";
    private String controlLoginPassword = "dounion";
    private String controlResetEnable = "false";
}
