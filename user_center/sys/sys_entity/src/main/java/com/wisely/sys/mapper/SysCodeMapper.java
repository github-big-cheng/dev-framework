package com.wisely.sys.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.sys.entity.SysCode;

import java.util.List;

/**
 * 系统代码表(SysCode)表数据库访问层
 *
 * @author system
 * @since 2022-09-05 10:53:16
 */
public interface SysCodeMapper extends BaseMapper<SysCode> {

    List<String> selectLocales();

}
