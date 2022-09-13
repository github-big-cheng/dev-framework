package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.ucenter.entity.UcenterOrgInfo;

/**
 * 机构信息详情表(UcenterOrgInfo)表数据库访问层
 *
 * @author system
 * @since 2022-09-07 14:21:27
 */
public interface UcenterOrgInfoMapper extends BaseMapper<UcenterOrgInfo> {

    int deleteByPersonId(Integer id);
}
