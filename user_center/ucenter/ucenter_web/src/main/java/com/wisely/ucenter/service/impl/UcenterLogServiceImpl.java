package com.wisely.ucenter.service.impl;

import com.wisely.framework.entity.PageVo;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterLog;
import com.wisely.ucenter.mapper.UcenterLogMapper;
import com.wisely.ucenter.service.UcenterLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;

@Service
public class UcenterLogServiceImpl implements UcenterLogService {

    @Resource
    UcenterLogMapper ucenterLogMapper;

    @Override
    public PageInfo findPage(UcenterLog query, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            query.setOrgIdQueryIn(UserHelper.getOrganizations());
        }
        query.setIsDeleted(0);
        return new PageInfo(ucenterLogMapper.selectListBySelective(query));
    }

    @Override
    public Integer add(UcenterLog ucenterLog) {
        return ucenterLogMapper.insertSelective(ucenterLog);
    }
}
