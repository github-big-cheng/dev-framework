package com.wisely.framework.plugins.redis;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.MultiRedisHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import com.wisely.framework.plugins.redis.jedis.JedisPoolOperation;
import com.wisely.framework.plugins.redis.lettuce.LettucePoolOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Redis多数据源 插件
 */
@Slf4j
@ConditionalOnProperty(prefix = "plugins.multi-redis", name = "enabled", havingValue = "true")
public class MultiRedisPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "MultiRedisPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.multi-redis")
    public MultiRedisProperties multiRedisProperties() {
        return new MultiRedisProperties();
    }

    @Bean
    public Map<String, RedisOperation> multiRedis(MultiRedisProperties multiRedisProperties) {
        Model<String, RedisOperation> multiRedis = Model.builder();
        multiRedisProperties.getDatasource().forEach((k, v) -> {
            handlePoolConfig(multiRedisProperties, v);
            multiRedis.set(k, redisOperation(v));
            log.info("Multi Redis datasource [{}] inited.", k);
        });
        MultiRedisHelper.init(multiRedisProperties.getPrimary(), multiRedisProperties.getStrict(), multiRedis);
        return multiRedis;
    }

    /**
     * RedisOperation 配置
     * 默认：基于LettucePool
     * 支持：JedisPool，LettucePool
     *
     * @param redisProperties 配置参数
     * @return redisOperation
     */
    public RedisOperation redisOperation(RedisProperties redisProperties) {
        return StringHelper.equals("jedis", redisProperties.getClient()) ?
                new JedisPoolOperation(redisProperties) : new LettucePoolOperation(redisProperties);
    }

    /**
     * 处理连接池配置
     *
     * @param multiRedisProperties redis多数据源属性
     * @param redisProperties      指定redis数据源属性
     */
    private void handlePoolConfig(MultiRedisProperties multiRedisProperties, RedisProperties redisProperties) {
        if (multiRedisProperties.getPool() != null) {
            if (StringHelper.equals("jedis", redisProperties.getClient())) {
                redisProperties.getJedis().setPool(multiRedisProperties.getPool());
            }
            if (StringHelper.equals("lettuce", redisProperties.getClient())) {
                redisProperties.getLettuce().setPool(multiRedisProperties.getPool());
            }
        }
    }
}
