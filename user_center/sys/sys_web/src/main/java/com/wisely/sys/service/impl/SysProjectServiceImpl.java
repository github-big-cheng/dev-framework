package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
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


    @Override
    public int save(SysProject record) {

        Integer userId = UserHelper.getUserId();
        String now = DateHelper.formatNow();

        if (ValidHelper.isEmpty(record.getId())) {
            record.setIsDeleted(0);
            record.setCreateBy(userId);
            record.setCreateTime(now);
            sysProjectMapper.insertSelective(record);
        } else {
            record.setUpdateBy(userId);
            record.setUpdateTime(now);
            sysProjectMapper.updateByPrimaryKeySelective(record);
        }
        return record.getId();
    }


    @Override
    public SysProject load(Integer id) {
        return sysProjectMapper.selectByPrimaryKey(id);
    }


    @Override
    public int delete(String idQueryIn) {
        SysProject record = new SysProject();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        sysProjectMapper.updateBySelective(record);
        return 1;
    }
}
