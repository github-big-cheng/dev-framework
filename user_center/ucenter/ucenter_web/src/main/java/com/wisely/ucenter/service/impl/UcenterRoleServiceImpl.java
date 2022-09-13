package com.wisely.ucenter.service.impl;


import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.caches.RoleCache;
import com.wisely.ucenter.entity.UcenterRole;
import com.wisely.ucenter.mapper.UcenterRoleMapper;
import com.wisely.ucenter.service.UcenterRoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;

/**
 * 角色表(UcenterRole) 服务实现类
 *
 * @author xi.wang
 * @date 2021/5/31 15:53
 */
@Service
public class UcenterRoleServiceImpl implements UcenterRoleService {

    @Resource
    UcenterRoleMapper ucenterRoleMapper;

    @Resource
    RoleCache roleCache;

    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    @Override
    public List<UcenterRole> findList(UcenterRole query) {
        query.setIsDeleted(0);
        return ucenterRoleMapper.selectListBySelective(query);
    }

    /**
     * 分页查询
     *
     * @param query  UcenterRole 对象
     * @param pageVo VoPageVo 对象
     * @return PageInfo 对象
     */
    @Override
    public PageInfo<UcenterRole> findByPage(UcenterRole query, PageVo pageVo) {
        Model model = RequestHelper.getInput();

        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        //不是超级管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            // 权限管理,角色管理，只展示所属机构的角色信息
            query.setOrgIdQueryIn(UserHelper.getOrganizations());
            // 人员新增时，特殊角色，多org共享
            if (model.isNotBlank("type")) {
                query.setOrgIdQueryIn(StringHelper.joinWith(",", 0, UserHelper.getOrganizations()));
            }
        }
        query.setIsDeleted(0);
        return new PageInfo<>(ucenterRoleMapper.selectListBySelective(query));
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public UcenterRole load(Integer id) {
        return ucenterRoleMapper.selectByPrimaryKey(id);
    }


    /**
     * 新增/更改
     *
     * @param record UcenterRole 对象
     * @return
     */
    @Override
    public int save(UcenterRole record) {
        String now = DateHelper.formatNow();

        //重复code校验
        UcenterRole query = new UcenterRole();
        query.setIsDeleted(0);
        query.setCode(record.getCode());
        query.setOrgId(UserHelper.getOrgId());
        List<UcenterRole> roleList = ucenterRoleMapper.selectListBySelective(query);

        if (ValidHelper.isNull(record.getId())) {
            AssertHelper.EX_VALIDATION.isEmpty(roleList, "ucenter_role_add.repeat_code_found.{0}", record.getCode());
            //新增
            record.setIsSys(0);
            record.setOrgId(UserHelper.getOrgId());
            record.setOrgName(UserHelper.getOrgName());
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(now);
            record.setIsDeleted(0);
            ucenterRoleMapper.insertSelective(record);
        } else {
            if (ValidHelper.isNotEmpty(roleList)) {
                roleList.forEach(ur ->
                        AssertHelper.EX_VALIDATION.isEquals(
                                ur.getId(),
                                record.getId(),
                                "ucenter_role_save.repeat_code_found.{0}", record.getCode()));
            }
            record.setOrgId(UserHelper.getOrgId());
            record.setOrgName(UserHelper.getOrgName());
            record.setUpdateTime(now);
            record.setUpdateBy(UserHelper.getUserId());
            ucenterRoleMapper.updateByPrimaryKeySelective(record);
        }

        UcenterRole entity = ucenterRoleMapper.selectByPrimaryKey(record.getId());
        roleCache.syncCache(entity);

        return record.getId();
    }


    /**
     * 批量删除
     *
     * @param idQueryIn
     * @return
     */
    @Override
    public int delete(String idQueryIn) {

        UcenterRole query = new UcenterRole();
        query.setIsDeleted(0);
        query.setIdQueryIn(idQueryIn);
        List<UcenterRole> roleList = ucenterRoleMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(roleList)) {
            return -1;
        }

        UcenterRole record = new UcenterRole();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateTime(DateHelper.formatNow());
        record.setUpdateBy(UserHelper.getUserId());
        ucenterRoleMapper.updateBySelective(record);

        roleCache.invalidate(roleList.toArray(new UcenterRole[]{}));
        return 0;
    }
}
