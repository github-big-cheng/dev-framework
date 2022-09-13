package com.wisely.sys.handler;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.eum.SysCacheEnum;
import com.wisely.sys.vo.SysCodeVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SysDictionaryHelper implements SysConstants {


    private final static String SPLIT_STR = "&&";

    /**
     * key为 缓存对应key + "&&" + id，减少缓存数量
     */
    private final static LoadingCache<String, Optional<EntityCacheManager>> ENTITY_CACHE_MANAGER_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(255) // 最大存储数量
                    .expireAfterWrite(3, TimeUnit.MINUTES) // 设置过期时间 3分钟 写入后三分钟失效
//                    .expireAfterAccess(3, TimeUnit.MINUTES) // 设置过期时间 3分钟 访问后三分钟失效 热key可能导致一直不失效，数据不一致问题
                    .build(new CacheLoader<String, Optional<EntityCacheManager>>() {
                        @Override
                        public Optional<EntityCacheManager> load(String key) {
                            String[] keys = StringHelper.split(key, SPLIT_STR);
                            SysCacheEnum sysCacheEnum = SysCacheEnum.loadByKey(keys[0]);
                            return Optional.fromNullable(findEntityCacheManager(sysCacheEnum, keys[1]));
                        }
                    });


    public static String loadValue(String bizKey, String values) {
        if (ValidHelper.isEmpty(values)) {
            return "";
        }
        // support multi keys
        List<String> list = Lists.newArrayList();
        String[] value = StringHelper.split(values, ",");
        for (String val : value) {
            list.add(loadEntityCacheManager(bizKey, val));
        }
        return StringHelper.join(list, ",");
    }


    public static String loadEntityCacheManager(String bizKey, String valueKey) {

        // 处理缓存的字典方式
        if (!SysCacheEnum.getMapperModel().containsKey(bizKey)) {
            return null;
        }

        SysCacheEnum sysCacheEnum = SysCacheEnum.loadByMapper(bizKey);
        EntityCacheManager entityCacheManager = null;
        boolean useL2Cache = false;
        if (useL2Cache) {
            // 二级缓存...是否有必要？
            try {
                entityCacheManager = ENTITY_CACHE_MANAGER_CACHE.get(sysCacheEnum.getKey() + SPLIT_STR + valueKey).orNull();
            } catch (Exception e) {
                log.error("UcenterDictionaryHelper.loadEntityCacheManager error:{}", e);
            }
        } else {
            // 使用redis序列化的方式
            entityCacheManager = findEntityCacheManager(sysCacheEnum, valueKey);
        }

        return Model.parseObject(entityCacheManager).getString(sysCacheEnum.getField());
    }


    public static EntityCacheManager findEntityCacheManager(SysCacheEnum sysCacheEnum, String valueKey) {
        try {
            // 使用redis序列化的方式
            return EntityCacheManager.deserializer(sysCacheEnum.getKey(), valueKey, sysCacheEnum.getClazz());
        } catch (Exception e) {
            log.error("UcenterDictionaryHelper.loadEntityCacheManager error:{}", e);
        }
        return null;
    }

    public static Model<String, EntityCacheManager> findEntityCacheManagers(SysCacheEnum sysCacheEnum, String... valueKeys) {
        try {
            // 使用redis序列化的方式
            return EntityCacheManager.batchDeserializer(
                    sysCacheEnum.getKey(),
                    (Class<EntityCacheManager>) sysCacheEnum.getClazz(),
                    valueKeys
            );
        } catch (Exception e) {
            log.error("UcenterDictionaryHelper.findEntityCacheManagers error:{}", e);
        }
        return null;
    }


    /**
     * 获取对应字典
     *
     * @param value
     * @return
     */
    public static String loadCodeName(String value) {
        SysCodeVo codeVo = loadCodeVo(value);
        return ValidHelper.isEmpty(codeVo) ? value : codeVo.getName();
    }

    /**
     * 获取对应字典
     *
     * @param values
     * @return
     */
    public static Model<String, String> loadCodeNames(String... values) {

        Model<String, String> result = Model.builder();
        if (ValidHelper.isEmpty(values)) {
            return result;
        }

        Model<String, SysCodeVo> codeVoModel =
                EntityCacheManager.batchDeserializer(
                        CODE_CACHE_KEY,
                        SysCodeVo.class,
                        values
                );

        codeVoModel.forEach((key, value) -> result.set(key, value.getName()));
        return result;
    }


    /**
     * 获取ucenterCodeVo对象
     *
     * @param value
     * @return
     */
    public static SysCodeVo loadCodeVo(String value) {
        if (ValidHelper.isBlank(value)) {
            return null;
        }

        return EntityCacheManager.deserializer(
                CODE_CACHE_KEY,
                value,
                SysCodeVo.class
        );
    }
}
