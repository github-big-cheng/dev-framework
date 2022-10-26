package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.framework.entity.Model;
import com.wisely.ucenter.entity.UcenterPerson;

import java.util.List;

/**
 * 人员(UcenterPerson)表数据库访问层
 *
 * @author system
 * @since 2021-07-26 09:28:24
 */
public interface UcenterPersonMapper extends BaseMapper<UcenterPerson> {

    /**
     * 获取指定角色的人员(如果有部门则获取指定部门指定角色的人员)
     *
     * @param model
     * @return
     */
    List<Model> loadPersonBySelective(Model model);

}
