package com.wisely.sys.common;

public interface SysConstants {

    /**
     * 系统代码默认国际化
     */
    String DEFAULT_LOCALE = "zh_CN";

    /**
     * 系统参数缓存
     */
    String PARAMETER_CACHE_KEY = "SYS_PARAMETER_CACHE:";

    /**
     * 代码缓存
     */
    String CODE_CACHE_KEY = "SYS_CODE_CACHE:";

    /**
     * 代码缓存
     */
    String CODE_MAPPER_KEY = "CODE_HASH";



    // =================================== NetApi ===================================================
    /**
     * 系统参数缓存
     */
    String UPLOAD_FILE = "/sysFile/add/api";

    /**
     * 获取多个files
     */
    String FIND_FILES = "/sysFile/list/api";

    /**
     * 删除file
     */
    String DELETE_FILE = "/sysFile/delete/api";

    /**
     * 菜单列表
     */
    String FUNCTION_FILE = "/sysFunction/list/api";

    /**
     * 项目列表
     */
    String PROJECT_LIST = "/sysProject/list/api";

}
