package com.wisely.ucenter.service.impl;

import com.google.common.base.Splitter;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonRole;
import com.wisely.ucenter.mapper.UcenterPersonRoleMapper;
import com.wisely.ucenter.service.UcenterPersonRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色人员中间表(UcenterPersonRole)表服务实现类
 *
 * @author xintao.li
 * @since 2021-05-31 16:35:54
 */
@Service
public class UcenterPersonRoleServiceImpl implements UcenterPersonRoleService {

    @Resource
    UcenterPersonRoleMapper personRoleMapper;

    @Override
    public int deleteByPersonId(Integer personId) {
        UcenterPersonRole ucenterPersonRole = new UcenterPersonRole();
        ucenterPersonRole.setPersonId(personId);
        ucenterPersonRole.setIsDeleted(1);
        ucenterPersonRole.setUpdateBy(UserHelper.getUserId());
        ucenterPersonRole.setUpdateTime(DateHelper.formatNow());
        return personRoleMapper.updateBySelective(ucenterPersonRole);
    }


    @Transactional
    @Override
    public void updatePersonRole(String roleIds, UcenterPerson ucenterPerson) {

        // 先将人员对象的角色信息删除
        personRoleMapper.deleteByPersonId(ucenterPerson.getId());

        if (ValidHelper.isBlank(roleIds)) {
            return;
        }

        // 添加人员对应的角色
        List<String> roleList = Splitter.on(",").splitToList(roleIds);
        roleList.forEach(d -> {
            UcenterPersonRole ucenterPersonRole = new UcenterPersonRole();
            ucenterPersonRole.setPersonId(ucenterPerson.getId());
            ucenterPersonRole.setRoleId(Integer.valueOf(d));
            ucenterPersonRole.setCreateBy(UserHelper.getUserId());
            ucenterPersonRole.setCreateTime(DateHelper.formatNow());
            ucenterPersonRole.setIsDeleted(0);
            personRoleMapper.insertSelective(ucenterPersonRole);
        });
    }

    @Override
    public List<UcenterPersonRole> list(UcenterPersonRole ucenterpersonrole) {
        ucenterpersonrole.setIsDeleted(0);
        return personRoleMapper.selectListBySelective(ucenterpersonrole);
    }
}
