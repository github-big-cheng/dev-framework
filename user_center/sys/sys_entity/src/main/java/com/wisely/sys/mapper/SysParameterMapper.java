package com.wisely.sys.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.sys.entity.SysParameter;

import java.util.List;

/**
 * 系统参数表(SysParameter)表数据库访问层
 *
 * @author system
 * @since 2022-09-05 11:02:09
 */
public interface SysParameterMapper extends BaseMapper<SysParameter> {

    List<Integer> selectOrgIds();
}
