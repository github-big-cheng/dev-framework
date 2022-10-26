package com.wisely.sys.mapper;

import com.wisely.sys.entity.SysColumnConfig;
import com.wisely.framework.entity.BaseMapper;

/**
 * 自定义页面配置表(SysColumnConfig)表数据库访问层
 *
 * @author system
 * @since 2021-06-02 18:01:12
 */
public interface SysColumnConfigMapper extends BaseMapper<SysColumnConfig> {

    void deleteByUserIdAndCode(SysColumnConfig query);
}