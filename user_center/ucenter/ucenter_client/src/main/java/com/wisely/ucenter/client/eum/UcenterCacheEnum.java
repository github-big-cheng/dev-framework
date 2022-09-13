package com.wisely.ucenter.client.eum;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.*;

import java.util.Arrays;


/**
 * Ucenter 缓存处理类
 */
public enum UcenterCacheEnum implements UcenterConstants {


    /**
     * UcenterOrg.id --> UcenterOrg.name
     */
    ORG_NAME(ORG_CACHE_KEY, "ORG_HASH", UcenterOrgVo.class, "cname", "id"),
    /**
     * UcenterPerson.id --> UcenterPerson.personName
     */
    PERSON_NAME(PERSON_CACHE_KEY, "PERSON_HASH", UcenterPersonVo.class, "name", "id"),
    /**
     * UcenterPerson.id --> UcenterPerson.account
     */
    PERSON_ACCOUNT(PERSON_CACHE_KEY, "ACCOUNT_HASH", UcenterPersonVo.class, "account", "id"),
    /**
     * UcenterPerson.account --> UcenterPerson.personName
     */
    ACCOUNT_PERSON(ACCOUNT_PERSON_CACHE_KEY, "ACCOUNT_PERSON_HASH", UcenterPersonVo.class, "name", "account"),

    /**
     * UcenterPerson.account --> UcenterPerson.personId
     */
    ACCOUNT_PERSONID(ACCOUNT_PERSON_CACHE_KEY, "ACCOUNT_PERSONID_HASH", UcenterPersonVo.class, "id", "account"),

    /**
     * UcenterUser.id --> UcenterUser.account
     */
    USER_ACCOUNT(USER_CACHE_KEY, "USER_HASH", UcenterUserVo.class, "account", "id"),
    /**
     * UcenterUser.id --> UcenterPerson.personName
     */
    USER_PERSONNAME(USER_PERSON_CACHE_KEY, "USER_PERSON_NAME_HASH", UcenterPersonVo.class, "name", "id"),

    /**
     * UcenterCode.value --> UcenterCode.name
     */
    ROLE_NAME(ROLE_CACHE_KEY, "ROLE_HASH", UcenterRoleVo.class, "name", "id"),

    /**
     * UcenterPosition.id -> UcenterPosition.name
     */
    POSITION_NAME(POSITION_CACHE_KEY, "POS_HASH", UcenterPositionVo.class, "name", "id"),
    ;

    private String key;

    private String mapper;

    private Class<? extends EntityCacheManager> clazz;

    private String field;

    private String primary;

    UcenterCacheEnum(String key, String mapper, Class<? extends EntityCacheManager> clazz, String field, String primary) {
        this.key = key;
        this.mapper = mapper;
        this.clazz = clazz;
        this.field = field;
        this.primary = primary;
    }

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

    private final static Model<String, UcenterCacheEnum> MAPPER_MODEL = Model.builder();

    private final static Model<String, UcenterCacheEnum> KEY_MODEL = Model.builder();

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

    public static UcenterCacheEnum loadByMapper(String mapper) {
        return MAPPER_MODEL.get(mapper);
    }

    public static UcenterCacheEnum loadByKey(String key) {
        return KEY_MODEL.get(key);
    }


}
