package com.wisely.ucenter.client.eum;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.handler.UcDictHelper;
import com.wisely.ucenter.client.vo.*;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

@Getter
public enum UcenterCacheEnum implements UcenterConstants {

    /**
     * 默认的空返回，避免空指针
     */
    EMPTY(null, (key) -> null),
    /**
     * UcenterOrg.id --> UcenterOrg.name
     */
    ORG_NAME("ORG_HASH", (orgId) -> {
        UcenterOrgVo orgVo = UcDictHelper.loadOrgVo(DataHelper.getInt(orgId));
        return ValidHelper.isNotEmpty(orgVo) ? orgVo.getCname() : null;
    }),
    /**
     * UcenterPerson.id --> UcenterPerson.name
     */
    PERSON_NAME("PERSON_HASH", (personId) -> {
        return UcDictHelper.getPersonName(DataHelper.getInt(personId));
    }),
    /**
     * UcenterPerson.id --> UcenterPerson.account
     */
    PERSON_ACCOUNT("ACCOUNT_HASH", (personId) -> {
        UcenterPersonVo personVo = UcDictHelper.loadPersonVo(DataHelper.getInt(personId));
        return ValidHelper.isNotEmpty(personVo) ? personVo.getAccount() : null;
    }),
//    /**
//     * UcenterPerson.account --> UcenterPerson.personName
//     */
//    ACCOUNT_PERSON(ACCOUNT_PERSON_CACHE_KEY, "ACCOUNT_PERSON_HASH", UcenterPersonVo.class, "name", "account"),
    /**
     * UcenterUser.id --> UcenterUser.account
     */
    USER_ACCOUNT("USER_HASH", (userId) -> {
        UcenterUserVo userVo = UcDictHelper.loadUser(DataHelper.getInt(userId));
        return ValidHelper.isNotEmpty(userVo) ? userVo.getAccount() : null;
    }),
    /**
     * UcenterUser.id --> UcenterPerson.name
     */
    USER_PERSON_NAME("USER_PERSON_NAME_HASH", (userId) -> {
        UcenterUserVo userVo = UcDictHelper.loadUser(DataHelper.getInt(userId));
        if (ValidHelper.isEmpty(userVo)) {
            return null;
        }

        return UcDictHelper.getPersonName(userVo.getPersonId());
    }),

    /**
     * UcenterRole.value --> UcenterRole.name
     */
    ROLE_NAME("ROLE_HASH", (roleId) -> {
        UcenterRoleVo roleVo = UcDictHelper.loadRoleVo(DataHelper.getInt(roleId));
        return ValidHelper.isNotEmpty(roleVo) ? roleVo.getName() : null;
    }),

    /**
     * UcenterPosition.id -> UcenterPosition.name
     */
    POSITION_NAME("POS_HASH", (positionId) -> {
        UcenterPositionVo positionVo = UcDictHelper.loadPositionVo(DataHelper.getInt(positionId));
        return ValidHelper.isNotEmpty(positionVo) ? positionVo.getName() : null;
    }),
    ;

    UcenterCacheEnum(String mapper, Function<String, String> valueFunction) {
        this.mapper = mapper;
        this.valueFunction = valueFunction;
    }

    private String mapper;

    private Function<String, String> valueFunction;


    private final static Model<String, UcenterCacheEnum> MAPPER_MODEL = Model.builder();

    static {
        Arrays.stream(values()).forEach(x -> {
            MAPPER_MODEL.set(x.mapper, x);
        });
    }

    public static Model getMapperModel() {
        return MAPPER_MODEL;
    }

    public static UcenterCacheEnum loadByMapper(String mapper) {
        return MAPPER_MODEL.containsKey(mapper) ? MAPPER_MODEL.get(mapper) : EMPTY;
    }

    public String loadValue(String key) {
        return this.getValueFunction().apply(key);
    }
}
