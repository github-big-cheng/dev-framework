package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.ucenter.entity.UcenterPersonRole;

/**
 * 角色人员中间表(UcenterPersonRole)表数据库访问层
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:36:09
 */
public interface UcenterPersonRoleMapper extends BaseMapper<UcenterPersonRole> {

    int deleteByPersonId(int personId);
}