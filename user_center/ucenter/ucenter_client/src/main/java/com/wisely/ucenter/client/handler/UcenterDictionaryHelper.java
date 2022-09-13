package com.wisely.ucenter.client.handler;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.eum.UcenterCacheEnum;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import com.wisely.ucenter.client.vo.UcenterPositionVo;
import com.wisely.ucenter.client.vo.UcenterUserVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class UcenterDictionaryHelper implements UcenterConstants {


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
                            UcenterCacheEnum ucenterCacheEnum = UcenterCacheEnum.loadByKey(keys[0]);
                            return Optional.fromNullable(findEntityCacheManager(ucenterCacheEnum, keys[1]));
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
        if (!UcenterCacheEnum.getMapperModel().containsKey(bizKey)) {
            return null;
        }

        UcenterCacheEnum ucenterCacheEnum = UcenterCacheEnum.loadByMapper(bizKey);
        EntityCacheManager entityCacheManager = null;
        boolean useL2Cache = false;
        if (useL2Cache) {
            // 二级缓存...是否有必要？
            try {
                entityCacheManager = ENTITY_CACHE_MANAGER_CACHE.get(ucenterCacheEnum.getKey() + SPLIT_STR + valueKey).orNull();
            } catch (Exception e) {
                log.error("UcenterDictionaryHelper.loadEntityCacheManager error:{}", e);
            }
        } else {
            // 使用redis序列化的方式
            entityCacheManager = findEntityCacheManager(ucenterCacheEnum, valueKey);
        }

        return Model.parseObject(entityCacheManager).getString(ucenterCacheEnum.getField());
    }


    public static EntityCacheManager findEntityCacheManager(UcenterCacheEnum ucenterCacheEnum, String valueKey) {
        try {

            // UcenterUser.id --> UcenterPerson.personName 特殊處理
            if (UcenterCacheEnum.USER_PERSONNAME.equals(ucenterCacheEnum)) {
                UcenterUserVo ucenterUserVo =
                        EntityCacheManager.deserializer(USER_CACHE_KEY, valueKey, UcenterUserVo.class);
                if (ValidHelper.isNull(ucenterUserVo)) {
                    return null;
                }

                return EntityCacheManager.deserializer(PERSON_CACHE_KEY,
                        DataHelper.getString(ucenterUserVo.getPersonId()),
                        UcenterPersonVo.class);
            }

            // 使用redis序列化的方式
            return EntityCacheManager.deserializer(ucenterCacheEnum.getKey(), valueKey, ucenterCacheEnum.getClazz());
        } catch (Exception e) {
            log.error("UcenterDictionaryHelper.loadEntityCacheManager error:{}", e);
        }
        return null;
    }

    public static Model<String, EntityCacheManager> findEntityCacheManagers(UcenterCacheEnum ucenterCacheEnum, String... valueKeys) {
        try {

            // UcenterUser.id --> UcenterPerson.personName 特殊處理
            if (UcenterCacheEnum.USER_PERSONNAME.equals(ucenterCacheEnum)) {
                Model<String, UcenterUserVo> userVoModel =
                        EntityCacheManager.batchDeserializer(USER_CACHE_KEY, UcenterUserVo.class, valueKeys);
                if (ValidHelper.isNull(userVoModel)) {
                    return null;
                }

                List<String> personIds =
                        userVoModel.keySet().stream()
                                .map(key -> DataHelper.getString(userVoModel.get(key).getPersonId()))
                                .collect(Collectors.toList());

                Model<String, UcenterPersonVo> personVoModel = EntityCacheManager.batchDeserializer(
                        PERSON_CACHE_KEY,
                        UcenterPersonVo.class,
                        personIds.toArray(new String[personIds.size()])
                );

                Model<String, EntityCacheManager> result = Model.builder();
                userVoModel.keySet().forEach(key -> {
                    result.set(key, personVoModel.get(DataHelper.getString(userVoModel.get(key).getPersonId())));
                });
                return result;
            }

            // 使用redis序列化的方式
            return EntityCacheManager.batchDeserializer(
                    ucenterCacheEnum.getKey(),
                    (Class<EntityCacheManager>) ucenterCacheEnum.getClazz(),
                    valueKeys
            );
        } catch (Exception e) {
            log.error("UcenterDictionaryHelper.findEntityCacheManagers error:{}", e);
        }
        return null;
    }


    /**
     * 获取人员信息
     *
     * @param personId
     * @return
     */
    public static UcenterPersonVo loadPersonVo(Integer personId) {
        if (ValidHelper.isEmpty(personId)) {
            return null;
        }
        return EntityCacheManager.deserializer(
                PERSON_CACHE_KEY,
                DataHelper.getString(personId),
                UcenterPersonVo.class);
    }


    /**
     * 人员姓名
     *
     * @param personId
     * @return
     */
    public static String getPersonName(Integer personId) {
        UcenterPersonVo personVo = loadPersonVo(personId);
        return ValidHelper.isEmpty(personVo) ? null : personVo.getName();
    }

    /**
     * 根据userId获取UcenterPerson
     *
     * @param userId
     * @return
     */
    public static UcenterPersonVo loadPersonVoByUserId(Integer userId) {
        if (ValidHelper.isEmpty(userId)) {
            return null;
        }
        UcenterUserVo ucenterUserVo =
                EntityCacheManager.deserializer(USER_CACHE_KEY, DataHelper.getString(userId), UcenterUserVo.class);
        return EntityCacheManager.deserializer(
                PERSON_CACHE_KEY,
                DataHelper.getString(ucenterUserVo.getPersonId()),
                UcenterPersonVo.class);
    }

    /**
     * 根据userId获取UcenterUser
     *
     * @param userId
     * @return
     */
    public static UcenterUserVo loadUser(Integer userId) {
        if (ValidHelper.isEmpty(userId)) {
            return null;
        }
        return EntityCacheManager.deserializer(
                USER_CACHE_KEY,
                DataHelper.getString(userId),
                UcenterUserVo.class);
    }

    /**
     * 通过account获取到UcenterPerson
     *
     * @param account
     * @return
     */
    public static UcenterPersonVo loadPersonByAccount(String account) {
        if (ValidHelper.isEmpty(account)) {
            return null;
        }
        return EntityCacheManager.deserializer(
                ACCOUNT_PERSON_CACHE_KEY,
                DataHelper.getString(account),
                UcenterPersonVo.class);
    }

    /**
     * 获取部门信息
     *
     * @param orgId
     * @return
     */
    public static UcenterOrgVo loadOrgVo(Integer orgId) {
        if (ValidHelper.isEmpty(orgId)) {
            return null;
        }
        return EntityCacheManager.deserializer(
                ORG_CACHE_KEY,
                DataHelper.getString(orgId),
                UcenterOrgVo.class);
    }


    /**
     * 获取单位/部门名臣
     *
     * @param orgId
     * @return
     */
    public static String getOrgName(Integer orgId) {
        UcenterOrgVo orgVo = loadOrgVo(orgId);
        return ValidHelper.isEmpty(orgVo) ? null : orgVo.getCname();
    }


    /**
     * 根据id获取职位
     *
     * @param positionId
     * @return
     */
    public static UcenterPositionVo loadPositionVo(Integer positionId) {
        if (ValidHelper.isEmpty(positionId)) {
            return null;
        }
        return EntityCacheManager.deserializer(POSITION_CACHE_KEY, DataHelper.getString(positionId), UcenterPositionVo.class);
    }
}
