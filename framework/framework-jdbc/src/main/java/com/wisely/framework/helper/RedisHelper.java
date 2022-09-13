package com.wisely.framework.helper;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.plugins.redis.RedisOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * Redis工具类
 * 使用外部RedisOperation
 */
@Slf4j
public class RedisHelper {

    private RedisHelper() {
    }


    public static RedisOperation REDIS_OPERATION;

    public static void init(RedisOperation operation) {
        if (REDIS_OPERATION == null) {
            REDIS_OPERATION = operation;
            log.info("RedisOperation装载已完成，可以通过RedisHelper类提供的方法来存取数据.");
        }
    }

    private static RedisOperation loadOperation() {
        if (REDIS_OPERATION == null) {
            throw SystemException.builder("{0} {1}",
                    "Use plugins.redis.enabled=true to enable redis plugin please.",
                    "If you had enabled, check com.dounion.framework.plugins.redis.RedisOperation bean has init?");
        }
        return REDIS_OPERATION;
    }


    /**
     * Pick up or use a resource to operation redis.
     * Please this resource make sure it will be return or closed.
     *
     * @param <T>
     * @return
     */
    public static <T> T getResource() {
        return (T) loadOperation().getResource();
    }


    /**
     * set String
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        return loadOperation().set(key, value);
    }


    /**
     * set String with expire time
     *
     * @param key
     * @param value
     * @param time  单位秒
     * @return
     */
    public static String set(String key, String value, int time) {
        return loadOperation().set(key, value, time);
    }


    /**
     * set multi String keys and values
     *
     * @param keysvalues
     * @return
     */
    public static String mset(String... keysvalues) {
        return loadOperation().mset(keysvalues);
    }

    /**
     * 保存字节数组
     *
     * @param key
     * @param value
     */
    public static String set(byte[] key, byte[] value) {
        return loadOperation().set(key, value);
    }

    /**
     * 保存字节数组
     *
     * @param key
     * @param value
     * @param time  秒
     */
    public static String set(byte[] key, byte[] value, int time) {
        return loadOperation().set(key, value, time);
    }

    /**
     * 保存字节数组
     *
     * @param key
     * @param value
     */
    public static String setBytes(String key, byte[] value) {
        return loadOperation().set(key.getBytes(), value);
    }

    /**
     * 保存字节数组
     *
     * @param key
     * @param value
     * @param time  秒
     */
    public static String setBytes(String key, byte[] value, int time) {
        return loadOperation().set(key.getBytes(), value, time);
    }


    /**
     * set multi byte[] keys and values
     *
     * @param keysvalues
     */
    public static String mset(byte[]... keysvalues) {
        return loadOperation().mset(keysvalues);
    }

    /**
     * get String
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return loadOperation().get(key);
    }

    /**
     * 获取字节数组
     *
     * @param key
     * @return
     */
    public static byte[] getBytes(String key) {
        return loadOperation().get(key.getBytes());
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public static Model<byte[], byte[]> mget(byte[]... keys) {
        Model<byte[], byte[]> model = Model.builder();
        List<byte[]> list = loadOperation().mget(keys);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < keys.length; i++) {
                model.set(keys[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 批量获取
     *
     * @param keys
     * @return
     */
    public static Model<String, String> mget(String... keys) {
        Model<String, String> model = Model.builder();
        List<String> results = loadOperation().mget(keys);
        if (ValidHelper.isNotEmpty(results)) {
            for (int i = 0; i < keys.length; i++) {
                model.set(keys[i], results.get(i));
            }
        }
        return model;
    }

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @return
     */
    public static String setNx(String key, String value) {
        return loadOperation().setNx(key, value);
    }


    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    public static String setNx(String key, String value, int time) {
        return loadOperation().setNx(key, value, time);
    }


    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @return
     */
    public static Long setNx(byte[] key, byte[] value) {
        return loadOperation().setNx(key, value);
    }


    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    public static String setNx(byte[] key, byte[] value, int time) {
        return loadOperation().setNx(key, value, time);
    }


    /**
     * 不存在时才设置
     *
     * @param keysvalues
     * @return
     */
    public static Long msetNx(byte[]... keysvalues) {
        return loadOperation().msetNx(keysvalues);
    }

    /**
     * 不存在时才设置
     *
     * @param keysvalues
     * @return
     */
    public static Long msetNx(String... keysvalues) {
        return loadOperation().msetNx(keysvalues);
    }

    /**
     * 删除指定key
     *
     * @param key
     * @return
     */
    public static Boolean del(String key) {
        return loadOperation().del(key);
    }

    /**
     * 获取key列表，支持模糊查询
     *
     * @param key
     * @return
     */
    public static Set<String> keys(String key) {
        return loadOperation().keys(key);
    }

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    public static Boolean exists(String key) {
        return loadOperation().exists(key);
    }

    /**
     * 续约
     *
     * @param key
     * @param seconds
     * @return
     */
    public static Long expire(String key, int seconds) {
        return loadOperation().expire(key, seconds);
    }

    /**
     * 获取剩余过期时间
     *
     * @param key
     * @return
     */
    public static Long ttl(String key) {
        return loadOperation().ttl(key);
    }

    /**
     * 自增
     *
     * @param key
     * @return
     */
    public static Long incr(String key) {
        return loadOperation().incr(key);
    }

    /**
     * 指定key 增加 指定数值
     *
     * @param key
     * @return
     */
    public static Long incr(String key, long value) {
        return loadOperation().incr(key, value);
    }

    /**
     * 自减
     *
     * @param key
     * @return
     */
    public static Long decr(String key) {
        return loadOperation().decr(key);
    }

    /**
     * 指定key 减少 指定数值
     *
     * @param key
     * @param value
     * @return
     */
    public static Long decr(String key, long value) {
        return loadOperation().decr(key, value);
    }

    /**
     * 获取hash表
     *
     * @param key
     * @return
     */
    public static Model<String, String> hgetAll(String key) {
        return loadOperation().hgetAll(key);
    }


    /**
     * 保存hash
     *
     * @param key
     * @param hash
     * @return
     */
    public static String hset(String key, Model<String, String> hash) {
        return loadOperation().hset(key, hash);
    }


    /**
     * 保存hash
     *
     * @param key
     * @param hash
     * @return
     */
    public static String hsetBytes(String key, Model<byte[], byte[]> hash) {
        return loadOperation().hset(key.getBytes(), hash);
    }

    /**
     * 保存hash
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public static Long hset(String key, String field, String value) {
        return loadOperation().hset(key, field, value);
    }

    /**
     * 获取hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    public static String hget(String key, String field) {
        return loadOperation().hget(key, field);
    }

    /**
     * 获取hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    public static byte[] hgetBytes(String key, String field) {
        return loadOperation().hget(key.getBytes(), field.getBytes());
    }


    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    public static Model<String, String> hmget(String key, String... fields) {
        Model model = Model.builder();
        List<String> list = loadOperation().hmget(key, fields);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < fields.length; i++) {
                model.set(fields[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    public static Model<String, byte[]> hmgetBytes(String key, String... fields) {

        byte[][] bytes = new byte[fields.length][];
        for (int i = 0; i < fields.length; i++) {
            bytes[i] = fields[i].getBytes();
        }

        Model model = Model.builder();
        List<byte[]> list = loadOperation().hmget(key.getBytes(), bytes);
        if (ValidHelper.isNotEmpty(list)) {
            for (int i = 0; i < fields.length; i++) {
                model.set(fields[i], list.get(i));
            }
        }
        return model;
    }

    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    public static Long hdel(String key, String... field) {
        return loadOperation().hdel(key, field);
    }

    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    public static Long hdel(byte[] key, byte[]... field) {
        return loadOperation().hdel(key, field);
    }


    /**
     * hash中指定key指定列增加指定数量
     *
     * @param key
     * @param field
     * @param incr
     * @return
     */
    public static Long hincr(String key, String field, long incr) {
        return loadOperation().hincr(key, field, incr);
    }


    /**
     * 移除指定key的生存时间
     *
     * @param key
     * @return
     */
    public static void persist(String key) {
        loadOperation().persist(key);
    }


    /**
     * 表头添加
     *
     * @param key
     * @param value
     */
    public static void lpush(String key, String... value) {
        loadOperation().lpush(key, value);
    }

    /**
     * 表头获取
     *
     * @param key
     */
    public static String lpop(String key) {
        return loadOperation().lpop(key);
    }

    /**
     * 表尾添加
     *
     * @param key
     * @param value
     */
    public static Long rpush(String key, String... value) {
        return loadOperation().rpush(key, value);
    }

    /**
     * 表尾获取
     *
     * @param key
     */
    public static String rpop(String key) {
        return loadOperation().rpop(key);
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
    public static List<String> lrange(String key, long start, long end) {
        return loadOperation().lrange(key, start, end);
    }

    /**
     * list：列表 ，num：删除的个数（有重复时），后add进去的值先被删，类似于出栈, element:删除的指定元素
     *
     * @param list
     * @param num
     * @param element
     */
    public static void lrem(String list, long num, String element) {
        loadOperation().lrem(list, num, element);
    }

    /**
     * 获取list长度
     *
     * @param key
     * @return
     */
    public static long llen(String key) {
        return loadOperation().llen(key);
    }
}
