package com.wisely.framework.helper.lock;


import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import org.springframework.stereotype.Component;

/**
 * 基于Redis实现的分布式锁
 */
@Component
public class RedisLock implements DoUnionLock {

    @Override
    public boolean lock(String key, String value, int time) {
        String result = RedisHelper.setNx(PREFIX + key, value, time);
        return ValidHelper.isNotEmpty(result);
    }


    @Override
    public boolean unlock(String key) {
        return RedisHelper.del(PREFIX + key);
    }

    @Override
    public String getValue(String key) {
        return RedisHelper.get(PREFIX + key);
    }
}
