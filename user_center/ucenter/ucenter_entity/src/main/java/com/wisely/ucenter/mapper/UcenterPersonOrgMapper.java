package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.framework.entity.Model;
import com.wisely.ucenter.entity.UcenterPersonOrg;

import java.util.List;

/**
 * 部门/机构人员中间表(UcenterPersonOrg)表数据库访问层
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:36:08
 */
public interface UcenterPersonOrgMapper extends BaseMapper<UcenterPersonOrg> {

    List<UcenterPersonOrg> selectQueryListBySelective(UcenterPersonOrg query);

    /**
     * 按人员删除相关部门
     *
     * @param model
     * @return
     */
    int deleteBySelective(Model model);
}
