package com.wisely.framework.plugins.redis;

import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import com.wisely.framework.plugins.redis.jedis.JedisPoolOperation;
import com.wisely.framework.plugins.redis.lettuce.LettucePoolOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Redis插件
 */
@ConditionalOnProperty(prefix = "plugins.redis", name = "enabled", havingValue = "true")
@Slf4j
public class RedisPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "RedisPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }


    /**
     * RedisOperation 配置
     * 默认：基于LettucePool
     * 支持：JedisPool，LettucePool
     *
     * @param redisProperties 配置参数
     * @return redisOperation
     */
    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean(RedisOperation.class)
    public RedisOperation redisOperation(RedisProperties redisProperties) {
        RedisOperation operation;
        if (StringHelper.equals("jedis", redisProperties.getClient())) {
            operation = new JedisPoolOperation(redisProperties);
        } else {
            operation = new LettucePoolOperation(redisProperties);
        }
        RedisHelper.init(operation); // 初始化操作类
        return operation;
    }
}
