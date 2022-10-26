package com.wisely.ucenter.client.common;

public interface UcenterConstants {

    // ======================================== 缓存相关 =====================================================
    /**
     * 机构缓存
     */
    String ORG_CACHE_KEY = "UCENTER_ORG_CACHE";

    /**
     * 人员缓存
     */
    String PERSON_CACHE_KEY = "UCENTER_PERSON_CACHE";

    /**
     * 帐号->人员的 缓存
     */
    String ACCOUNT_PERSON_CACHE_KEY = "ACCOUNT_PERSON_CACHE_KEY";
    /**
     * 用户缓存
     */
    String USER_CACHE_KEY = "UCENTER_USER_CACHE";

    /**
     * 职位缓存
     */
    String POSITION_CACHE_KEY = "UCENTER_POSITION_CACHE";
    /**
     * 机构缓存
     */
    String ROLE_CACHE_KEY = "UCENTER_ROLE_CACHE";


    // ======================================== 相关UcenterNetApi相关 =====================================================
    /**
     * 获取SsoUser接口
     */
    String LOAD_SSO_USER = "/ucenterUser/loadUser/api";

    /**
     * 机构/部门列表
     */
    String ORG_LIST = "/ucenterOrg/list/api";

    /**
     * 机构/部门分页
     */
    String ORG_PAGE = "/ucenterOrg/page/api";

    /**
     * 人员列表查询
     */
    String PERSON_LIST = "/ucenterPerson/loadPersonBySelective/api";

    /**
     * 人员分页查询
     */
    String PERSON_PAGE = "/ucenterPerson/list/api";
}
