package com.wisely.framework.plugins.redis.lettuce;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.plugins.redis.RedisOperation;
import com.wisely.framework.plugins.redis.RedisProperties;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LettucePoolOperation implements RedisOperation<RedisConnection> {

    public LettucePoolOperation(RedisProperties redisProperties) {

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        if (redisProperties.getLettuce() != null && redisProperties.getLettuce().getPool() != null) {
            RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
            poolConfig.setMaxIdle(pool.getMaxIdle());
            poolConfig.setMinIdle(pool.getMinIdle());
            poolConfig.setMaxTotal(pool.getMaxActive());
            poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
            poolConfig.setTestOnBorrow(pool.isTestOnBorrow());
        }

        //连接池配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
                LettucePoolingClientConfiguration.builder()
                        .commandTimeout(redisProperties.getTimeout())
                        .shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout())
                        .poolConfig(poolConfig);
        LettuceClientConfiguration clientConfig = builder.build();
        // 创建连接工厂
        RedisStandaloneConfiguration redisConfiguration =
                new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        redisConfiguration.setPassword(redisProperties.getPassword());
        redisConfiguration.setDatabase(redisProperties.getDatabase());
        LettuceConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisConfiguration, clientConfig);
        //属性配置后续处理
        redisConnectionFactory.afterPropertiesSet();
        this.connectionFactory = redisConnectionFactory;
    }

    private RedisConnectionFactory connectionFactory;

    @Override
    public RedisConnection getResource() {
        try {
            if (connectionFactory == null) {
                throw SystemException.builder("please use plugins.redis.enabled=true to enable redis plugin");
            }
            return connectionFactory.getConnection();
        } catch (Exception e) {
            throw SystemException.builder(e.getMessage(), e);
        }
    }

    /**
     * 销毁链接池
     */
    @Override
    public void destroy() {

    }


    /**
     * k-v数组转map
     *
     * @param keysValues
     * @return
     */
    private Map convert(Object[] keysValues) {
        Map map = new HashMap();
        for (int i = 0; i < keysValues.length; i += 2) {
            Object key = keysValues[i];
            Object value = keysValues[i + 1];
            map.put(key, value);
        }
        return map;
    }

    /**
     * k-v数组转map
     *
     * @param keysValues
     * @return
     */
    private Map<byte[], byte[]> convertStringArrayToByteMap(String[] keysValues) {
        Map<byte[], byte[]> map = new HashMap<>();
        for (int i = 0; i < keysValues.length; i += 2) {
            String key = keysValues[i];
            String value = keysValues[i + 1];
            map.put(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
        }
        return map;
    }

    /**
     * k-v数组转map
     *
     * @param keysValues
     * @return
     */
    private Map<byte[], byte[]> convertByteArrayToByteMap(byte[][] keysValues) {
        Map<byte[], byte[]> map = new HashMap<>();
        for (int i = 0; i < keysValues.length; i += 2) {
            map.put(keysValues[i], keysValues[i + 1]);
        }
        return map;
    }

    /**
     * k-v String转byte
     *
     * @param keysValues
     * @return
     */
    private Map<byte[], byte[]> convertStringMapToByteMap(Model<String, String> keysValues) {
        Map<byte[], byte[]> map = new HashMap<>();
        if (ValidHelper.isNotEmpty(keysValues)) {
            keysValues.forEach((key, value) ->
                    map.put(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8))
            );
        }
        return map;
    }

    /**
     * string数组转byte array
     *
     * @param keys
     * @return
     */
    private byte[][] convertStringArrayToByteArray(String[] keys) {
        byte[][] bytes = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            bytes[i] = keys[i].getBytes(StandardCharsets.UTF_8);
        }
        return bytes;
    }


    /**
     * 保存字节数组
     *
     * @param key
     * @param bytes
     * @return
     */
    @Override
    public String set(byte[] key, byte[] bytes) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection.set(key, bytes);
            return new String(key, StandardCharsets.UTF_8);
        }
    }


    /**
     * set with expire time
     *
     * @param key
     * @param value
     * @param time  单位秒
     * @return
     */
    @Override
    public String set(byte[] key, byte[] value, int time) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection
                    .set(
                            key, value, Expiration.from(time, TimeUnit.SECONDS),
                            RedisStringCommands.SetOption.UPSERT
                    );
            return new String(key, StandardCharsets.UTF_8);
        }
    }

    /**
     * set String
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(String key, String value) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection
                    .set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
            return key;
        }
    }

    /**
     * set String with expire time
     *
     * @param key
     * @param value
     * @param time  单位秒
     * @return
     */
    @Override
    public String set(String key, String value, int time) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection
                    .set(
                            key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                            Expiration.from(time, TimeUnit.SECONDS),
                            RedisStringCommands.SetOption.UPSERT
                    );
            return key;
        }
    }

    /**
     * 批量字节数组保存
     *
     * @param keysvalues
     * @return
     */
    @Override
    public String mset(byte[]... keysvalues) {
        try (RedisConnection redisConnection = getResource()) {
            Map map = convert(keysvalues);
            redisConnection.mSet(map);
            return String.valueOf(map.size());
        }
    }

    /**
     * 批量设置
     *
     * @param keysvalues
     * @return
     */
    @Override
    public String mset(String... keysvalues) {
        try (RedisConnection redisConnection = getResource()) {
            Map<byte[], byte[]> map = convertStringArrayToByteMap(keysvalues);
            redisConnection.mSet(map);
            return String.valueOf(map.size());
        }
    }


    /**
     * 获取字节数组
     *
     * @param key
     * @return
     */
    @Override
    public byte[] get(byte[] key) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.get(key);
        }
    }


    /**
     * get String
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        try (RedisConnection redisConnection = getResource()) {
            byte[] bytes = redisConnection.get(key.getBytes(StandardCharsets.UTF_8));
            return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "";
        }
    }

    /**
     * 批量取数
     *
     * @param keys 字节数组
     * @return
     */
    @Override
    public List<byte[]> mget(byte[]... keys) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.mGet(keys);
        }
    }

    /**
     * 批量取数
     *
     * @param keys
     * @return
     */
    @Override
    public List<String> mget(String... keys) {
        try (RedisConnection redisConnection = getResource()) {
            List<String> retList = Lists.newArrayList();
            List<byte[]> list = redisConnection.mGet(convertStringArrayToByteArray(keys));
            if (ValidHelper.isNotEmpty(list)) {
                list.forEach(item -> retList.add(item != null ? new String(item, StandardCharsets.UTF_8) : ""));
            }
            return retList;
        }
    }


    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String setNx(String key, String value) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection
                            .setNX(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8))
            ) ? key : null;
        }
    }

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    @Override
    public String setNx(String key, String value, int time) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection
                            .set(
                                    key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                                    Expiration.from(time, TimeUnit.SECONDS),
                                    RedisStringCommands.SetOption.SET_IF_ABSENT
                            )
            ) ? key : null;
        }
    }

    /**
     * 不存在时才设置
     * 批量
     *
     * @param keysvalues
     * @return
     */
    @Override
    public Long msetNx(String... keysvalues) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection.mSetNX(convertStringArrayToByteMap(keysvalues))
            ) ? keysvalues.length : 0L;
        }
    }

    /**
     * 不存在时才设置-字节数组
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long setNx(byte[] key, byte[] value) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(redisConnection.setNX(key, value)) ? 1L : 0L;
        }
    }

    /**
     * 不存在时才设置 - 指定超时时间
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    @Override
    public String setNx(byte[] key, byte[] value, int time) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection
                            .set(
                                    key, value,
                                    Expiration.from(time, TimeUnit.SECONDS),
                                    RedisStringCommands.SetOption.SET_IF_ABSENT
                            )
            ) ? new String(key, StandardCharsets.UTF_8) : null;
        }
    }


    /**
     * 不存在时才设置 - 批量
     *
     * @param keysvalues
     * @return
     */
    @Override
    public Long msetNx(byte[]... keysvalues) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(redisConnection
                    .mSetNX(convertByteArrayToByteMap(keysvalues))) ? keysvalues.length : 0L;
        }
    }


    /**
     * 删除指定key
     *
     * @param key
     * @return
     */
    @Override
    public Boolean del(String key) {
        try (RedisConnection redisConnection = getResource()) {
            Long result = redisConnection.del(key.getBytes(StandardCharsets.UTF_8));
            return result != null && result > -1L;
        }
    }

    /**
     * 获取key列表，支持模糊查询
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> keys(String key) {
        try (RedisConnection redisConnection = getResource()) {
            Set<String> returnSet = new HashSet<>();
            Set<byte[]> sets = redisConnection.keys(key.getBytes(StandardCharsets.UTF_8));
            if (ValidHelper.isNotEmpty(sets)) {
                sets.forEach(item -> returnSet.add(item != null ? new String(item, StandardCharsets.UTF_8) : ""));
            }
            return returnSet;
        }
    }

    /**
     * 检查是否存在
     *
     * @param key
     * @return
     */
    @Override
    public Boolean exists(String key) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.exists(key.getBytes(StandardCharsets.UTF_8));
        }
    }


    /**
     * 续约
     *
     * @param key
     * @param seconds
     * @return
     */
    @Override
    public Long expire(String key, int seconds) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection.expire(key.getBytes(StandardCharsets.UTF_8), seconds)
            ) ? 1L : 0L;
        }
    }


    /**
     * 获取剩余过期时间
     *
     * @param key
     * @return
     */
    @Override
    public Long ttl(String key) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.ttl(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 自增
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.incr(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 指定key增加指定数值
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long incr(String key, long value) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.incrBy(key.getBytes(StandardCharsets.UTF_8), value);
        }
    }

    /**
     * 自减
     *
     * @param key
     * @return
     */
    @Override
    public Long decr(String key) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.decr(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 指定key 减少 指定数值
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long decr(String key, long value) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.decrBy(key.getBytes(StandardCharsets.UTF_8), value);
        }
    }


    /**
     * 获取hash表
     *
     * @param key
     * @return
     */
    @Override
    public Model<String, String> hgetAll(String key) {
        try (RedisConnection redisConnection = getResource()) {
            Model<String, String> model = Model.builder();
            Map<byte[], byte[]> map = redisConnection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
            if (ValidHelper.isNotEmpty(map)) {
                map.forEach((k, v) -> {
                    if (k != null) {
                        model.put(
                                new String(k, StandardCharsets.UTF_8),
                                v != null ? new String(v, StandardCharsets.UTF_8) : ""
                        );
                    }
                });
            }
            return model;
        }
    }

    /**
     * hash表中批量添加
     *
     * @param key
     * @param hash
     * @return
     */
    @Override
    public String hset(String key, Model<String, String> hash) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection.hMSet(key.getBytes(StandardCharsets.UTF_8), convertStringMapToByteMap(hash));
            return key;
        }
    }


    /**
     * 保存hash
     *
     * @param key
     * @param hash
     * @return
     */
    @Override
    public String hset(byte[] key, Model<byte[], byte[]> hash) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection.hMSet(key, hash);
            return String.valueOf(hash.size());
        }
    }


    /**
     * 保存hash
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hset(String key, String field, String value) {
        try (RedisConnection redisConnection = getResource()) {
            return Boolean.TRUE.equals(
                    redisConnection.hSet(
                            key.getBytes(StandardCharsets.UTF_8),
                            field.getBytes(StandardCharsets.UTF_8),
                            value.getBytes(StandardCharsets.UTF_8)
                    )
            ) ? 1L : 0L;
        }
    }

    /**
     * 获取hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(String key, String field) {
        try (RedisConnection redisConnection = getResource()) {
            byte[] bytes = redisConnection
                    .hGet(key.getBytes(StandardCharsets.UTF_8), field.getBytes(StandardCharsets.UTF_8));
            return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "";
        }
    }


    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<String> hmget(String key, String... fields) {
        try (RedisConnection redisConnection = getResource()) {
            List<String> returnList = Lists.newArrayList();
            List<byte[]> list = redisConnection
                    .hMGet(key.getBytes(StandardCharsets.UTF_8), convertStringArrayToByteArray(fields));
            if (ValidHelper.isNotEmpty(list)) {
                list.forEach(bytes ->
                        returnList.add(bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "")
                );
            }
            return returnList;
        }
    }


    /**
     * 获取hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public byte[] hget(byte[] key, byte[] field) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.hGet(key, field);
        }
    }


    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    @Override
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.hMGet(key, fields);
        }
    }


    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Long hdel(String key, String... field) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection
                    .hDel(key.getBytes(StandardCharsets.UTF_8), convertStringArrayToByteArray(field));
        }
    }

    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Long hdel(byte[] key, byte[]... field) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection.hDel(key, field);
        }
    }

    /**
     * hash中指定key指定列增加指定数量
     *
     * @param key
     * @param field
     * @param incr
     * @return
     */
    @Override
    public Long hincr(String key, String field, long incr) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection
                    .hIncrBy(key.getBytes(StandardCharsets.UTF_8), field.getBytes(StandardCharsets.UTF_8), incr);
        }
    }


    /**
     * 移除指定key的生存时间
     *
     * @param key
     */
    @Override
    public void persist(String key) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection.persist(key.getBytes(StandardCharsets.UTF_8));
        }
    }


    /**
     * 表头添加
     *
     * @param key
     * @param value
     */
    @Override
    public void lpush(String key, String... value) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection.lPush(key.getBytes(StandardCharsets.UTF_8), convertStringArrayToByteArray(value));
        }
    }

    /**
     * 表头获取
     *
     * @param key
     */
    @Override
    public String lpop(String key) {
        try (RedisConnection redisConnection = getResource()) {
            byte[] bytes = redisConnection.lPop(key.getBytes(StandardCharsets.UTF_8));
            return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "";
        }
    }

    /**
     * 表尾添加
     *
     * @param key
     * @param value
     */
    @Override
    public Long rpush(String key, String... value) {
        try (RedisConnection redisConnection = getResource()) {
            return redisConnection
                    .rPush(key.getBytes(StandardCharsets.UTF_8), convertStringArrayToByteArray(value));
        }
    }

    /**
     * 表尾获取
     *
     * @param key
     */
    @Override
    public String rpop(String key) {
        try (RedisConnection redisConnection = getResource()) {
            byte[] bytes = redisConnection.rPop(key.getBytes(StandardCharsets.UTF_8));
            return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "";
        }
    }

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 使用负数下标时，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     */
    @Override
    public List<String> lrange(String key, long start, long end) {
        try (RedisConnection redisConnection = getResource()) {
            List<String> returnList = Lists.newArrayList();
            List<byte[]> list =
                    redisConnection.lRange(key.getBytes(StandardCharsets.UTF_8), start, end);
            if (ValidHelper.isNotEmpty(list)) {
                list.forEach(bytes ->
                        returnList.add(bytes != null ? new String(bytes, StandardCharsets.UTF_8) : "")
                );
            }
            return returnList;
        }
    }

    /**
     * list：列表 ，num：删除的个数（有重复时），后add进去的值先被删，类似于出栈, element:删除的指定元素
     *
     * @param list
     * @param num
     * @param element
     */
    @Override
    public void lrem(String list, long num, String element) {
        try (RedisConnection redisConnection = getResource()) {
            redisConnection
                    .lRem(list.getBytes(StandardCharsets.UTF_8), num, element.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 获取list长度
     *
     * @param key
     * @return
     */
    @Override
    public long llen(String key) {
        try (RedisConnection redisConnection = getResource()) {
            Long len = redisConnection.lLen(key.getBytes(StandardCharsets.UTF_8));
            return len != null ? len : 0L;
        }
    }

}
