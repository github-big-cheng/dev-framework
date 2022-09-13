package com.wisely.ucenter.service;


import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonRole;

import java.util.List;

/**
 * 角色人员中间表(TUcenterPersonRole)表服务接口
 *
 * @author xintao.li
 * @since 2021-05-31 16:33:06
 */
public interface UcenterPersonRoleService {

    /**
     * 删除某个personId对应的人员角色关系
     *
     * @param personID personID
     * @return int
     */
    int deleteByPersonId(Integer personID);

    /**
     * 修改人员对应的角色关系
     * @param roleIds 批量角色id 逗号分隔
     * @param ucenterPerson 人员对象
     */
    void updatePersonRole(String roleIds, UcenterPerson ucenterPerson);

    /**
     * 动态查询
     * @param ucenterpersonrole UcenterPersonRole
     * @return List<UcenterPersonRole>
     */
    List<UcenterPersonRole> list(UcenterPersonRole ucenterpersonrole);


}
