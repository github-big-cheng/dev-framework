package com.wisely.ucenter.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.caches.PositionCache;
import com.wisely.ucenter.entity.UcenterPosition;
import com.wisely.ucenter.mapper.UcenterPositionMapper;
import com.wisely.ucenter.service.UcenterPositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UcenterPositionServiceImpl implements UcenterPositionService, SsoConstants {

    @Resource
    UcenterPositionMapper ucenterPositionMapper;

    @Resource
    PositionCache positionCache;


    @Override
    public PageInfo findByPage(UcenterPosition query, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            query.setOrgIdQueryIn(UserHelper.getOrganizations());
        }
        query.setIsDeleted(0);
        return new PageInfo(ucenterPositionMapper.selectListBySelective(query));
    }

    @Override
    public int save(UcenterPosition record) {
        String now = DateHelper.formatNow();
        //重复code校验
        UcenterPosition ucenterPosition = new UcenterPosition();
        ucenterPosition.setOrgId(UserHelper.getOrgId());
        ucenterPosition.setCode(record.getCode());
        ucenterPosition.setIsDeleted(0);
        List<UcenterPosition> ucenterPositions = ucenterPositionMapper.selectListBySelective(ucenterPosition);

        if (ValidHelper.isEmpty(record.getId())) {
            AssertHelper.EX_VALIDATION.isEmpty(ucenterPositions, "ucenter_position_save.repeat_code_found.{0}", record.getCode());
            // 新增
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(now);
            record.setOpState(0);
            record.setOrgId(UserHelper.getOrgId());
            record.setOrgName(UserHelper.getOrgName());
            record.setIsDeleted(0);
            ucenterPositionMapper.insert(record);
        } else {
            // 修改
            if (ValidHelper.isNotEmpty(ucenterPositions)) {
                ucenterPositions.forEach(up -> {
                    AssertHelper.EX_VALIDATION.isEquals(up.getId(), record.getId(), "ucenter_position_save.repeat_code_found.{0}", record.getCode());
                });
            }
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(now);
            record.setIsDeleted(0);
            ucenterPositionMapper.updateByPrimaryKeySelective(record);
        }

        // 刷新缓存
        positionCache.syncCache(record);
        return record.getId();
    }

    @Override
    public UcenterPosition load(Integer id) {
        return ucenterPositionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(String ids) {
        UcenterPosition record = new UcenterPosition();
        record.setIsDeleted(1);
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        record.setIdQueryIn(ids);
        return ucenterPositionMapper.updateBySelective(record);
    }
}
