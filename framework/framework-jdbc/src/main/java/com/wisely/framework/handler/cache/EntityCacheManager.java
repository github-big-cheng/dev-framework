package com.wisely.framework.handler.cache;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;

public interface EntityCacheManager {
    
    /**
     * 反序列化
     *
     * @return
     */
    static <T extends EntityCacheManager> T deserializer(String cacheKey, String primaryKey, Class<T> clazz) {
//        return batchDeserializer(cacheKey, clazz, primaryKey).get(primaryKey);
        AssertHelper.EX_SYSTEM.isNotBlank(cacheKey, "common.parameter_required.cacheKey");
        AssertHelper.EX_SYSTEM.isNotBlank(primaryKey, "common.parameter_required.primaryKey");
        AssertHelper.EX_SYSTEM.isNotEmpty(clazz, "common.parameter_required.clazz");

        byte[] bytes = RedisHelper.hgetBytes(cacheKey, primaryKey);
        if (ValidHelper.isEmpty(bytes)) {
            return null;
        }
        return ProtoBufHelper.deserializer(bytes, clazz);
    }

    /**
     * 反序列化
     * 使用redis的批处理
     *
     * @return
     */
    static <T extends EntityCacheManager> Model<String, T> batchDeserializer(String cacheKey, Class<T> clazz, String... primaryKeys) {

        AssertHelper.EX_SYSTEM.isNotBlank(cacheKey, "common.parameter_required.cacheKey");
        AssertHelper.EX_SYSTEM.isNotEmpty(primaryKeys, "common.parameter_required.primaryKey");
        AssertHelper.EX_SYSTEM.isNotEmpty(clazz, "common.parameter_required.clazz");

        Model<String, T> result = Model.builder();

        Model<String, byte[]> bytes = RedisHelper.hmgetBytes(cacheKey, primaryKeys);
        if (ValidHelper.isEmpty(bytes)) {
            return result;
        }

        bytes.forEach((key, value) -> {
            if (ValidHelper.isEmpty(value)) {
                return;
            }
            result.set(key, ProtoBufHelper.deserializer(value, clazz));
        });
        return result;
    }
}
