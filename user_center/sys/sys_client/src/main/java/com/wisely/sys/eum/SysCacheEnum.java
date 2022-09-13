package com.wisely.sys.eum;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.vo.SysCodeVo;

import java.util.Arrays;

public enum SysCacheEnum implements SysConstants {


    /**
     * SysCode.value --> SysCode.name
     */
    CODE_VALUE(CODE_CACHE_KEY, "CODE_HASH", SysCodeVo.class, "name", "value");


    SysCacheEnum(String key, String mapper, Class<? extends EntityCacheManager> clazz, String field, String primary) {
        this.key = key;
        this.mapper = mapper;
        this.clazz = clazz;
        this.field = field;
        this.primary = primary;
    }

    private String key;

    private String mapper;

    private Class<? extends EntityCacheManager> clazz;

    private String field;

    private String primary;


    public String getKey() {
        return key;
    }

    public String getMapper() {
        return mapper;
    }

    public Class<? extends EntityCacheManager> getClazz() {
        return clazz;
    }

    public String getField() {
        return field;
    }

    public String getPrimary() {
        return primary;
    }

    private final static Model<String, SysCacheEnum> MAPPER_MODEL = Model.builder();

    private final static Model<String, SysCacheEnum> KEY_MODEL = Model.builder();

    static {
        Arrays.stream(values()).forEach(x -> {
            MAPPER_MODEL.set(x.getMapper(), x);
            KEY_MODEL.set(x.getKey(), x);
        });
    }

    public static Model getMapperModel() {
        return MAPPER_MODEL;
    }

    public static Model getKeyModel() {
        return KEY_MODEL;
    }

    public static SysCacheEnum loadByMapper(String mapper) {
        return MAPPER_MODEL.get(mapper);
    }

    public static SysCacheEnum loadByKey(String key) {
        return KEY_MODEL.get(key);
    }
}
