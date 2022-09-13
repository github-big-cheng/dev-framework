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
     * 用户ID-人員缓存
     */
    String USER_PERSON_CACHE_KEY = "UCENTER_USER_PERSON_CACHE";

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
     * 获取SSOUSER接口
     */
    String LOAD_SSOUSER = "/ucenterUser/loadUser/api";

    /**
     * 新增UcenterPerson
     */
    String ADD_PERSON = "/ucenterPerson/add/api";
    /**
     * 编辑UcenterPerson
     */
    String EDIT_PERSON = "/ucenterPerson/save/api";
    /**
     * 删除人员以及对应的关系信息
     */
    String DELETE_PERSON = "/ucenterPerson/delete/api";
    /**
     * 锁定
     */
    String LOCK = "/ucenterPerson/lock/api";
    /**
     * 解锁
     */
    String UN_LOCK = "/ucenterPerson/unlock/api";
    /**
     * 注销
     */
    String DESTROY = "/ucenterPerson/destroy/api";

    /**
     * 查询机构
     */
    String ORG_LIST = "/ucenterOrg/list/api";

    /**
     * 部门信息(树)
     */
    String DEPT_TREE = "/ucenterOrg/tree";

    /**
     * 部门人员信息(树)
     */
    String DEPT_PERSON_TREE = "/ucenterOrg/listDeptPerson/tree";

    /**
     * 获取角色
     */
    String ROLE_LIST = "/ucenterRole/list/api";

    /**
     * 获取人员信息
     */
    String PERSON_LIST = "/ucenterPerson/combox/api";

    /**
     * 获取各部门负责人信息
     */
    String SELECTIVE_PERSON = "/ucenterPerson/loadPersonBySelective/api";

    /**
     * 部门人员信息
     */
    String DEPT_PERSON = "/ucenterDeptPerson/list/api";
}
