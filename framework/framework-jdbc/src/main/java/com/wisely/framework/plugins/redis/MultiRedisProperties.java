package com.wisely.framework.plugins.redis;

import lombok.Data;

import java.util.Map;

/**
 * redis 多数据源属性配置
 *
 * 示例：
 *      #开启 多数据源
 *      plugins.multi-redis.enabled=true
 *      #设置默认的数据源
 *      plugins.multi-redis.primary=master
 *      #设置严格模式,默认false不启动. 启动后在未匹配到指定数据源时候回抛出异常,不启动会使用默认数据源.
 *      plugins.multi-redis.strict=true
 *      #可以配置全局连接池参数 每个数据源连接池都使用全局参数配置
 *      #plugins.multi-redis.pool.*=
 *      # master
 *      plugins.multi-redis.datasource.master.host=ip
 *      plugins.multi-redis.datasource.master.port=port
 *      plugins.multi-redis.datasource.master.password=password
 *      plugins.multi-redis.datasource.master.database=database
 *      # 可以配置单个数据源连接池参数
 *      #plugins.multi-redis.datasource.master.pool.*=
 *      # slave
 *      plugins.multi-redis.datasource.slave.host=ip
 *      plugins.multi-redis.datasource.slave.port=port
 *      plugins.multi-redis.datasource.slave.password=password
 *      plugins.multi-redis.datasource.slave.database=database
 *      #others
 *      ...
 *
 */
@Data
public class MultiRedisProperties {

    /**
     * 必须设置默认的数据源，默认master
     */
    private String primary = "master";

    /**
     * 是否启用严格模式，默认不启动。严格模式下未匹配到数据源直接报错，非严格模式下则使用 primary 所设置的数据源
     */
    private Boolean strict = false;

    /**
     * 每一个数据源
     * 示 例：
     *      # master
     *      plugins.multi-redis.datasource.master.host=ip
     *      plugins.multi-redis.datasource.master.port=port
     *      plugins.multi-redis.datasource.master.password=password
     *      plugins.multi-redis.datasource.master.database=database
     *      # 可以配置单个数据源连接池参数
     *      #plugins.multi-redis.datasource.master.pool.*=
     *      # slave
     *      plugins.multi-redis.datasource.slave.host=ip
     *      plugins.multi-redis.datasource.slave.port=port
     *      plugins.multi-redis.datasource.slave.password=password
     *      plugins.multi-redis.datasource.slave.database=database
     *      # 可以配置单个数据源连接池参数
     *      #plugins.multi-redis.datasource.slave.pool.*=
     */
    private Map<String, RedisProperties> datasource;

    /**
     * 全局连接池参数
     *      可以通过 plugins.multi-redis.pool.*= 配置全局连接池参数
     */
    private RedisProperties.Pool pool;
}
