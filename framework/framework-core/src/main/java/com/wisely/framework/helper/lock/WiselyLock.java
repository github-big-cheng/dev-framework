package com.wisely.framework.helper.lock;

/**
 * 锁
 */
public interface WiselyLock {

    /**
     * 锁名前缀（加锁该前缀后，可以将所有的Redis锁放入一个分组，方便查看）
     */
    String PREFIX = "Wisely-LOCK:";

    /**
     * 加锁
     *
     * @param key 锁名
     * @param time 超时时间
     * @return true=加锁成功；false=加锁失败
     */
    default boolean lock(String key, int time) {
        return lock(key, key, time);
    }


    /**
     * 加锁
     *
     * @param key 锁名
     * @param value 特定值
     * @param time 超时时间
     * @return true=加锁成功；false=加锁失败
     */
    boolean lock(String key, String value, int time);

    /**
     * 解锁
     *
     * @param key 锁名
     * @return true=解锁成功；false=解锁失败
     */
    boolean unlock(String key);


    /**
     * 获取存入的内容
     * @param key
     * @return
     */
    String getValue(String key);
}
