package com.wisely.framework.plugins.redis.jedis;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.framework.plugins.redis.RedisOperation;
import com.wisely.framework.plugins.redis.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.Set;

@Slf4j
public class JedisPoolOperation implements RedisOperation<Jedis> {

    public JedisPoolOperation(RedisProperties redisProperties) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        if (redisProperties.getJedis() != null && redisProperties.getJedis().getPool() != null) {
            RedisProperties.Pool pool = redisProperties.getJedis().getPool();
            poolConfig.setMinIdle(pool.getMinIdle());
            poolConfig.setMaxIdle(pool.getMaxIdle());
            poolConfig.setMaxTotal(pool.getMaxActive());
            poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }

        String password = ValidHelper.isEmpty(redisProperties.getPassword()) ? null : redisProperties.getPassword();
        int timeout = DataHelper.getInt(redisProperties.getTimeout().toMillis(), -1);
        this.pool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort(), timeout, password, redisProperties.getDatabase());
    }

    private JedisPool pool;


    /**
     * 销毁链接池
     */
    @Override
    public void destroy() {
        if (pool != null) {
            pool.destroy();
        }
    }

    /**
     * Pick up or use a resource to operation redis.
     * Please this resource make sure it will be return or closed.
     *
     * @return
     */
    public Jedis getResource() {
        if (pool == null) {
            throw SystemException.builder("please use plugins.redis.enabled=true to enable redis plugin");
        }
        return pool.getResource();
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
        try (Jedis jedis = getResource()) {
            return jedis.set(key, bytes);
        }
    }


    @Override
    public String set(byte[] key, byte[] value, int time) {
        try (Jedis jedis = getResource()) {
            return jedis.set(key, value, SetParams.setParams().ex(time));
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
        try (Jedis jedis = getResource()) {
            return jedis.set(key, value);
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
        try (Jedis jedis = getResource()) {
            String set = jedis.set(key, value, SetParams.setParams().ex(time));
            return set;
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
        try (Jedis jedis = getResource()) {
            return jedis.mset(keysvalues);
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
        try (Jedis jedis = getResource()) {
            return jedis.mset(keysvalues);
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
        try (Jedis jedis = getResource()) {
            return jedis.get(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.get(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.mget(keys);
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
        try (Jedis jedis = getResource()) {
            return jedis.mget(keys);
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
        try (Jedis jedis = getResource()) {
            return jedis.setnx(key, value) > 0 ? key : null;
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
        try (Jedis jedis = getResource()) {
            // 修改为原子性命令
            return jedis.set(key, value, SetParams.setParams().nx().ex(time));
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
        try (Jedis jedis = getResource()) {
            return jedis.msetnx(keysvalues);
        }
    }


    @Override
    public Long setNx(byte[] key, byte[] value) {
        try (Jedis jedis = getResource()) {
            return jedis.setnx(key, value);
        }
    }

    @Override
    public String setNx(byte[] key, byte[] value, int time) {
        try (Jedis jedis = getResource()) {
            return jedis.set(key, value, SetParams.setParams().nx().ex(time));
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
    public Long msetNx(byte[]... keysvalues) {
        try (Jedis jedis = getResource()) {
            return jedis.msetnx(keysvalues);
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
        try (Jedis jedis = getResource()) {
            return jedis.del(key) > 0;
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
        try (Jedis jedis = getResource()) {
            return jedis.keys(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.exists(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.expire(key, seconds);
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
        try (Jedis jedis = getResource()) {
            return jedis.ttl(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.incr(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.incrBy(key, value);
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
        try (Jedis jedis = getResource()) {
            return jedis.decr(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.decrBy(key, value);
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
        try (Jedis jedis = getResource()) {
            return Model.builder(jedis.hgetAll(key));
        }
    }

    /**
     * @param key
     * @param hash
     * @return
     */
    @Override
    public String hset(String key, Model<String, String> hash) {
        try (Jedis jedis = getResource()) {
            return jedis.hmset(key, hash); // redis版本过低 hmset高版本已标记弃用 推荐hset
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
        try (Jedis jedis = getResource()) {
            return jedis.hmset(key, hash); // redis版本过低 hmset高版本已标记弃用 推荐hset
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
        try (Jedis jedis = getResource()) {
            return jedis.hset(key, field, value);
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
        try (Jedis jedis = getResource()) {
            return jedis.hget(key, field);
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
        try (Jedis jedis = getResource()) {
            return jedis.hmget(key, fields);
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
        try (Jedis jedis = getResource()) {
            return jedis.hget(key, field);
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
        try (Jedis jedis = getResource()) {
            return jedis.hmget(key, fields);
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
        try (Jedis jedis = getResource()) {
            return jedis.hdel(key, field);
        }
    }

    @Override
    public Long hdel(byte[] key, byte[]... field) {
        try (Jedis jedis = getResource()) {
            return jedis.hdel(key, field);
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
        try (Jedis jedis = getResource()) {
            return jedis.hincrBy(key, field, incr);
        }
    }


    /**
     * 移除指定key的生存时间
     *
     * @param key
     */
    @Override
    public void persist(String key) {
        try (Jedis jedis = getResource()) {
            jedis.persist(key);
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
        try (Jedis jedis = getResource()) {
            jedis.lpush(key, value);
        }
    }

    /**
     * 表头获取
     *
     * @param key
     * @return
     */
    @Override
    public String lpop(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.lpop(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.rpush(key, value);
        }
    }

    /**
     * 表尾获取
     *
     * @param key
     */
    @Override
    public String rpop(String key) {
        try (Jedis jedis = getResource()) {
            return jedis.rpop(key);
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
        try (Jedis jedis = getResource()) {
            return jedis.lrange(key, start, end);
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
        try (Jedis jedis = getResource()) {
            jedis.lrem(list, num, element);
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
        try (Jedis jedis = getResource()) {
            return jedis.llen(key);
        }
    }
}
