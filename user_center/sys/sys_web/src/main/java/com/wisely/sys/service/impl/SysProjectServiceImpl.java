package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.PageVo;
import com.wisely.sys.entity.SysProject;
import com.wisely.sys.mapper.SysProjectMapper;
import com.wisely.sys.service.SysProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysProjectServiceImpl implements SysProjectService {


    @Resource
    SysProjectMapper sysProjectMapper;

    @Override
    public PageInfo list(SysProject query, PageVo pageVo) {
        query.setIsDeleted(0);
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        return new PageInfo(sysProjectMapper.selectListBySelective(query));
    }
}
