package com.wisely.framework.plugins.redis;

import com.wisely.framework.entity.Model;

import java.util.List;
import java.util.Set;


/**
 * redis操作类
 */
public interface RedisOperation<T> {


    /**
     * Pick up or use a redis operation resource.
     * Please make sure the resource will be return or closed.
     *
     * @return
     */
    T getResource();


    void destroy();


    /**
     * 保存字节数组
     *
     * @param key
     * @param value
     * @return
     */
    String set(byte[] key, byte[] value);


    /**
     * set with expire time
     *
     * @param key
     * @param value
     * @param time  单位秒
     * @return
     */
    String set(byte[] key, byte[] value, int time);

    /**
     * set String
     *
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);

    /**
     * set String with expire time
     *
     * @param key
     * @param value
     * @param time  单位秒
     * @return
     */
    String set(String key, String value, int time);

    /**
     * 批量字节数组保存
     * ps: 未提供过期时间，使用需谨慎
     *
     * @param keysvalues
     * @return
     */
    String mset(byte[]... keysvalues);

    /**
     * 批量设置
     *
     * @param keysvalues
     * @return
     */
    String mset(String... keysvalues);

    /**
     * 获取字节数组
     *
     * @param key
     * @return
     */
    byte[] get(byte[] key);

    /**
     * get String
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 批量取值
     * 字节数组
     *
     * @param keys
     * @return
     */
    List<byte[]> mget(byte[]... keys);

    /**
     * 批量取值
     *
     * @param keys
     * @return
     */
    List<String> mget(String... keys);

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @return
     */
    String setNx(String key, String value);

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    String setNx(String key, String value, int time);

    /**
     * 不存在时才设置
     * -- 批量方式
     *
     * @param keysvalues
     * @return
     */
    Long msetNx(String... keysvalues);

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @return
     */
    Long setNx(byte[] key, byte[] value);

    /**
     * 不存在时才设置
     *
     * @param key
     * @param value
     * @param time  过期时间单位秒
     * @return
     */
    String setNx(byte[] key, byte[] value, int time);

    /**
     * 不存在时才设置
     * 批量
     *
     * @param keysvalues
     * @return
     */
    Long msetNx(byte[]... keysvalues);

    /**
     * 删除指定key
     *
     * @param key
     * @return
     */
    Boolean del(String key);

    /**
     * 获取key列表，支持模糊查询
     *
     * @param key
     * @return
     */
    Set<String> keys(String key);

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    Boolean exists(String key);

    /**
     * 续约
     *
     * @param key
     * @param seconds
     * @return
     */
    Long expire(String key, int seconds);

    /**
     * 获取剩余过期时间
     *
     * @param key
     * @return
     */
    Long ttl(String key);

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long incr(String key);

    /**
     * 指定key 增加 指定数值
     * @param key
     * @param value
     * @return
     */
    Long incr(String key, long value);

    /**
     * 自减
     * @param key
     * @return
     */
    Long decr(String key);

    /**
     * 指定key 减少 指定数值
     * @param key
     * @param value
     * @return
     */
    Long decr(String key, long value);

    /**
     * 获取hash表
     *
     * @param key
     * @return
     */
    Model<String, String> hgetAll(String key);

    /**
     * 保存hash
     *
     * @param key
     * @param hash
     * @return
     */
    String hset(String key, Model<String, String> hash);


    /**
     * 保存hash
     *
     * @param key
     * @param hash
     * @return
     */
    String hset(byte[] key, Model<byte[], byte[]> hash);

    /**
     * 保存hash
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hset(String key, String field, String value);

    /**
     * 获取hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param field
     * @return
     */
    List<String> hmget(String key, String... field);

    /**
     * 获取指定
     *
     * @param key
     * @param field
     * @return
     */
    byte[] hget(byte[] key, byte[] field);

    /**
     * 获取hash中多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    List<byte[]> hmget(byte[] key, byte[]... fields);

    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    Long hdel(String key, String... field);


    /**
     * 删除hash中某个字段
     *
     * @param key
     * @param field
     * @return
     */
    Long hdel(byte[] key, byte[]... field);

    /**
     * hash中指定key指定列增加指定数量
     *
     * @param key
     * @param field
     * @param incr
     * @return
     */
    Long hincr(String key, String field, long incr);

    /**
     * 移除指定key的生存时间
     *
     * @param key
     */
    void persist(String key);

    /**
     * 表头添加
     *
     * @param key
     * @param value
     */
    void lpush(String key, String... value);

    /**
     * 表头获取
     * @param key
     */
    String lpop(String key);

    /**
     * 表尾添加
     * @param key
     * @param value
     */
    Long rpush(String key, String... value);

    /**
     * 表尾获取
     * @param key
     */
    String rpop(String key);

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
     * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
     * 使用负数下标时，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     *
     * @param key
     * @param start
     * @param end
     */
    List<String> lrange(String key, long start, long end);


    /**
     * list：列表 ，num：删除的个数（有重复时），后add进去的值先被删，类似于出栈, element:删除的指定元素
     *
     * @param list
     * @param num
     * @param element
     */
    void lrem(String list, long num, String element);


    /**
     * 获取list长度
     *
     * @param key
     * @return
     */
    long llen(String key);
}
