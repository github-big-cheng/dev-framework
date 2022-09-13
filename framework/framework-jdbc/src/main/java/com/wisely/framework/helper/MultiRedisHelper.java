package com.wisely.framework.helper;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.plugins.redis.RedisOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * Redis 多数据源操作 工具类
 */
@Slf4j
public class MultiRedisHelper {

    private MultiRedisHelper() {
    }

    /**
     * 默认的数据源
     */
    private static String PRIMARY;

    /**
     * 是否启用严格模式，默认不启动。严格模式下未匹配到数据源直接报错，非严格模式下则使用 primary 所设置的数据源
     */
    private static boolean STRICT = false;

    /**
     * 多个数据源名称、操作对象集合
     */
    private static Model<String, RedisOperation> MULTI_REDIS;

    /**
     * 初始化
     *
     * @param primary    默认数据源
     * @param strict     是否严格模式
     * @param multiRedis 多数据源集合
     */
    public static void init(String primary, Boolean strict, Model<String, RedisOperation> multiRedis) {
        if (StringHelper.isEmpty(PRIMARY)) {
            PRIMARY = primary;
        }

        STRICT = strict != null && strict;

        if (MULTI_REDIS == null) {
            MULTI_REDIS = multiRedis;
            log.info("Redis多数据源装载已完成，可以通过MultiRedisHelper类提供的方法来指定数据源存取数据.");
        }
    }

    /**
     * 获取操作对象
     *
     * @param dataSource 数据源名称
     * @return 操作对象
     */
    private static RedisOperation loadOperation(String dataSource) {
        if (MULTI_REDIS == null) {
            throw SystemException.builder(
                    "{0} {1}",
                    "Use [plugins.multi-redis.enabled=true] to enable multi redis plugin please.",
                    "If you had enabled, check multi redis RedisOperation bean has init?"
            );
        }
        RedisOperation redisOperation = MULTI_REDIS.get(dataSource);
        if (redisOperation != null) {
            return redisOperation;
        }

        // 严格模式下 未匹配到数据源 抛出异常
        AssertHelper.EX_SYSTEM.isTrue(
                !(redisOperation == null && STRICT),
                "Multi Redis datasource [{0}] not matched, check it right?",
                dataSource);

        // 非严格模式下 未匹配到数据源 返回primary设置的默认数据源
        redisOperation = MULTI_REDIS.get(PRIMARY);
        AssertHelper.EX_SYSTEM.isNotNull(redisOperation, "Multi Redis datasource [{0}] not matched, check it right?", dataSource);

        return redisOperation;
    }


    /**
     * 获取redis操作资源
     *
     * @param dataSource 数据源名称
     * @return 操作资源对象
     */
    public static <T> T getResource(String dataSource) {
        return (T) loadOperation(dataSource).getResource();
    }


    /**
     * 保存字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 结果
     */
    public static String set(String dataSource, String key, String value) {
        return loadOperation(dataSource).set(key, value);
    }


    /**
     * 保存字符串 并设置过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @param time       过期时间 单位秒
     * @return 结果
     */
    public static String set(String dataSource, String key, String value, int time) {
        return loadOperation(dataSource).set(key, value, time);
    }


    /**
     * 批量 保存字符串
     *
     * @param dataSource 数据源名称
     * @param keysvalues key、value数据
     * @return 结果
     */
    public static String mset(String dataSource, String... keysvalues) {
        return loadOperation(dataSource).mset(keysvalues);
    }

    /**
     * 保存字节数组
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 结果
     */
    public static String set(String dataSource, byte[] key, byte[] value) {
        return loadOperation(dataSource).set(key, value);
    }

    /**
     * 保存字节数组 并设置过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @param time       过期时间 单位秒
     * @return 结果
     */
    public static String set(String dataSource, byte[] key, byte[] value, int time) {
        return loadOperation(dataSource).set(key, value, time);
    }

    /**
     * 保存字节数组
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 结果
     */
    public static String setBytes(String dataSource, String key, byte[] value) {
        return loadOperation(dataSource).set(key.getBytes(), value);
    }

    /**
     * 保存字节数组 并设置过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @param time       过期时间 单位秒
     * @return 结果
     */
    public static String setBytes(String dataSource, String key, byte[] value, int time) {
        return loadOperation(dataSource).set(key.getBytes(), value, time);
    }


    /**
     * 批量 保存字节数组
     *
     * @param dataSource 数据源名称
     * @param keysvalues 键、值内容
     * @return 结果
     */
    public static String mset(String dataSource, byte[]... keysvalues) {
        return loadOperation(dataSource).mset(keysvalues);
    }

    /**
     * 获取字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 字符串
     */
    public static String get(String dataSource, String key) {
        return loadOperation(dataSource).get(key);
    }

    /**
     * 获取字节数组
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 字节数组
     */
    public static byte[] getBytes(String dataSource, String key) {
        return loadOperation(dataSource).get(key.getBytes());
    }

    /**
     * 批量 获取字节数组
     *
     * @param dataSource 数据源名称
     * @param keys       key集合
     * @return 字节数组集合
     */
    public static Model<byte[], byte[]> mget(String dataSource, byte[]... keys) {
        Model<byte[], byte[]> model = Model.builder();
        List<byte[]> list = loadOperation(dataSource).mget(keys);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < keys.length; i++) {
                model.set(keys[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 批量 获取字符串
     *
     * @param dataSource 数据源名称
     * @param keys       key集合
     * @return 字符串集合
     */
    public static Model<String, String> mget(String dataSource, String... keys) {
        Model<String, String> model = Model.builder();
        List<String> results = loadOperation(dataSource).mget(keys);
        if (ValidHelper.isNotEmpty(results)) {
            for (int i = 0; i < keys.length; i++) {
                model.set(keys[i], results.get(i));
            }
        }
        return model;
    }

    /**
     * 不存在时 才保存字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 结果
     */
    public static String setNx(String dataSource, String key, String value) {
        return loadOperation(dataSource).setNx(key, value);
    }


    /**
     * 不存在时 才保存字符串 带过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @param time       过期时间 单位秒
     * @return 结果
     */
    public static String setNx(String dataSource, String key, String value, int time) {
        return loadOperation(dataSource).setNx(key, value, time);
    }


    /**
     * 不存在时 才保存字节数组
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 结果
     */
    public static Long setNx(String dataSource, byte[] key, byte[] value) {
        return loadOperation(dataSource).setNx(key, value);
    }


    /**
     * 不存在时 才保存字节数组 带过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @param time       过期时间 单位秒
     * @return 结果
     */
    public static String setNx(String dataSource, byte[] key, byte[] value, int time) {
        return loadOperation(dataSource).setNx(key, value, time);
    }


    /**
     * 批量 不存在时 才保存字节数组
     *
     * @param dataSource 数据源名称
     * @param keysvalues 键、值数据
     * @return 结果
     */
    public static Long msetNx(String dataSource, byte[]... keysvalues) {
        return loadOperation(dataSource).msetNx(keysvalues);
    }

    /**
     * 批量 不存在时 才保存字符串
     *
     * @param dataSource 数据源名称
     * @param keysvalues 键、值数据
     * @return 结果
     */
    public static Long msetNx(String dataSource, String... keysvalues) {
        return loadOperation(dataSource).msetNx(keysvalues);
    }

    /**
     * 删除指定key
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 操作结果
     */
    public static Boolean del(String dataSource, String key) {
        return loadOperation(dataSource).del(key);
    }

    /**
     * 获取key列表，支持模糊查询
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 集合
     */
    public static Set<String> keys(String dataSource, String key) {
        return loadOperation(dataSource).keys(key);
    }

    /**
     * 是否存在指定key
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 结果
     */
    public static Boolean exists(String dataSource, String key) {
        return loadOperation(dataSource).exists(key);
    }

    /**
     * 续约
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param seconds    过期时间 单位秒
     * @return 结果
     */
    public static Long expire(String dataSource, String key, int seconds) {
        return loadOperation(dataSource).expire(key, seconds);
    }

    /**
     * 获取剩余过期时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 过期时间
     */
    public static Long ttl(String dataSource, String key) {
        return loadOperation(dataSource).ttl(key);
    }

    /**
     * 指定key自增
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 操作结果
     */
    public static Long incr(String dataSource, String key) {
        return loadOperation(dataSource).incr(key);
    }

    /**
     * 指定key 增加 指定数值
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 操作结果
     */
    public static Long incr(String dataSource, String key, long value) {
        return loadOperation(dataSource).incr(key, value);
    }

    /**
     * 指定key 自减
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 操作结果
     */
    public static Long decr(String dataSource, String key) {
        return loadOperation(dataSource).decr(key);
    }

    /**
     * 指定key 减少 指定数值
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 操作结果
     */
    public static Long decr(String dataSource, String key, long value) {
        return loadOperation(dataSource).decr(key, value);
    }

    /**
     * 获取hash表
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 集合
     */
    public static Model<String, String> hgetAll(String dataSource, String key) {
        return loadOperation(dataSource).hgetAll(key);
    }


    /**
     * 保存hash
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param hash       model
     * @return 结果
     */
    public static String hset(String dataSource, String key, Model<String, String> hash) {
        return loadOperation(dataSource).hset(key, hash);
    }


    /**
     * 保存hash
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param hash       model
     * @return 结果
     */
    public static String hsetBytes(String dataSource, String key, Model<byte[], byte[]> hash) {
        return loadOperation(dataSource).hset(key.getBytes(), hash);
    }

    /**
     * 保存hash 字段 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param field      属性
     * @param value      内容
     * @return 操作结果
     */
    public static Long hset(String dataSource, String key, String field, String value) {
        return loadOperation(dataSource).hset(key, field, value);
    }

    /**
     * 获取hash中某个字段 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param field      属性
     * @return 字符串
     */
    public static String hget(String dataSource, String key, String field) {
        return loadOperation(dataSource).hget(key, field);
    }

    /**
     * 获取hash中某个字段 字节数组
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param field      属性
     * @return 字节数组
     */
    public static byte[] hgetBytes(String dataSource, String key, String field) {
        return loadOperation(dataSource).hget(key.getBytes(), field.getBytes());
    }


    /**
     * 获取hash中多个字段 字符串集合
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param fields     属性集合
     * @return 字符串集合
     */
    public static Model<String, String> hmget(String dataSource, String key, String... fields) {
        Model model = Model.builder();
        List<String> list = loadOperation(dataSource).hmget(key, fields);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < fields.length; i++) {
                model.set(fields[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 获取hash中多个字段 字节数组集合
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param fields     属性集合
     * @return 字节数组集合
     */
    public static Model<String, byte[]> hmgetBytes(String dataSource, String key, String... fields) {

        byte[][] bytes = new byte[fields.length][];
        for (int i = 0; i < fields.length; i++) {
            bytes[i] = fields[i].getBytes();
        }

        Model model = Model.builder();
        List<byte[]> list = loadOperation(dataSource).hmget(key.getBytes(), bytes);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < fields.length; i++) {
                model.set(fields[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 批量 删除hash中字段
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param field      属性
     * @return 操作结果
     */
    public static Long hdel(String dataSource, String key, String... field) {
        return loadOperation(dataSource).hdel(key, field);
    }

    /**
     * hash中指定key 指定列 增加指定数量
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param field      属性
     * @param incr       自增值
     * @return 操作结果
     */
    public static Long hincr(String dataSource, String key, String field, long incr) {
        return loadOperation(dataSource).hincr(key, field, incr);
    }


    /**
     * 移除指定key的生存时间
     *
     * @param dataSource 数据源名称
     * @param key        key
     */
    public static void persist(String dataSource, String key) {
        loadOperation(dataSource).persist(key);
    }


    /**
     * 表头添加 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     */
    public static void lpush(String dataSource, String key, String... value) {
        loadOperation(dataSource).lpush(key, value);
    }

    /**
     * 表头获取 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 字符串
     */
    public static String lpop(String dataSource, String key) {
        return loadOperation(dataSource).lpop(key);
    }

    /**
     * 表尾添加 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param value      内容
     * @return 操作结果
     */
    public static Long rpush(String dataSource, String key, String... value) {
        return loadOperation(dataSource).rpush(key, value);
    }

    /**
     * 表尾获取 字符串
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 字符串
     */
    public static String rpop(String dataSource, String key) {
        return loadOperation(dataSource).rpop(key);
    }

    /**
     * 获取指定列表 指定范围数据
     * <p>
     * 参数start和end都以0为底，
     * 0表示列表的第一个元素，1表示列表的第二个元素，以此类推。
     * -1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @param start      开始索引
     * @param end        结束索引
     * @return 数据列表
     */
    public static List<String> lrange(String dataSource, String key, long start, long end) {
        return loadOperation(dataSource).lrange(key, start, end);
    }

    /**
     * 删除指定列表 指定元素 指定删除个数（有重复时）
     * <p>
     * 后add进去的值先被删，类似于出栈
     *
     * @param dataSource 数据源名称
     * @param list       指定列表
     * @param num        删除数量
     * @param element    删除的指定元素
     */
    public static void lrem(String dataSource, String list, long num, String element) {
        loadOperation(dataSource).lrem(list, num, element);
    }

    /**
     * 获取list长度
     *
     * @param dataSource 数据源名称
     * @param key        key
     * @return 列表长度
     */
    public static long llen(String dataSource, String key) {
        return loadOperation(dataSource).llen(key);
    }
}
