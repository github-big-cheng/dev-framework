package com.wisely.framework.helper.lock;

import com.wisely.framework.helper.ValidHelper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;


public class LocalLock implements DoUnionLock {


    public LocalLock(int expireTime) {
        this.LOCK_CACHES =
                CacheBuilder.newBuilder()
//                        .maximumSize(1000) // 最大数量
                        .expireAfterWrite(expireTime, TimeUnit.MINUTES) // 10秒后失效
                        .build();
    }

    private Cache<String, String> LOCK_CACHES;

    @Override
    public boolean lock(String key, String value, int time) {
        // putIfAbsent 失败返回null
        String val = LOCK_CACHES.asMap().putIfAbsent(PREFIX + key, value);
        return !ValidHelper.isNull(val);
    }

    @Override
    public boolean unlock(String key) {
        LOCK_CACHES.invalidate(PREFIX + key);
        return true;
    }

    @Override
    public String getValue(String key) {
        return LOCK_CACHES.getIfPresent(PREFIX + key);
    }
}
