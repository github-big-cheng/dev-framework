package com.wisely.sys.service;


import com.wisely.sys.entity.SysColumnConfig;
import com.wisely.framework.entity.Model;

import java.util.List;

/**
 * 自定义页面配置表(TSysColumnConfig)表服务接口
 *
 * @author xintao.li
 * @since 2021-06-01 17:56:46
 */
public interface SysColumnConfigService {
    /**
     * 客户自定义页面配置查询
     *      未配置则返回系统默认配置
     * @param query
     * @return
     */
    List<SysColumnConfig> customConfigList(SysColumnConfig query);


    /**
     * 配置全集查询
     * @param query
     * @return
     */
    List<Model> fullList(SysColumnConfig query);

    /**
     * 保存
     * @param records
     * @return
     */
    int save(List<SysColumnConfig> records);


    /**
     * 还原系统默认页面配置
     * @param record
     */
    void reset(SysColumnConfig record);
}
