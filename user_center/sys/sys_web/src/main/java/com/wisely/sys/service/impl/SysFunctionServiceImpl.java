package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.entity.SysFunction;
import com.wisely.sys.mapper.SysFunctionMapper;
import com.wisely.sys.service.SysFunctionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能(TUcenterFunction)表服务实现类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:42:03
 */
@Service
public class SysFunctionServiceImpl implements SysFunctionService, SsoConstants {

    @Resource
    SysFunctionMapper sysFunctionMapper;

    /**
     * 分页查询
     *
     * @param query
     * @param pageVo
     * @return
     */
    @Override
    public PageInfo findByPage(SysFunction query, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        query.setIsDeleted(0);
        return new PageInfo(sysFunctionMapper.selectListBySelective(query));
    }

    /**
     * 新增/修改
     *
     * @param record
     * @return
     */
    @Override
    public int save(SysFunction record) {
        if (ValidHelper.isEmpty(record.getId())) {
            SysFunction function = sysFunctionMapper.selectByPrimaryKey(record.getParentId());
            if (ValidHelper.isNotEmpty(function)) {
                record.setProjectId(function.getProjectId());
            }
            // 新增
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(DateHelper.formatNow());
            record.setIsDeleted(0);
            sysFunctionMapper.insert(record);
        } else {
            // 修改
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            sysFunctionMapper.updateByPrimaryKey(record);
        }
        return record.getId();
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @Override
    public Model view(Integer id) {
        SysFunction entity = sysFunctionMapper.selectByPrimaryKey(id);
        Model model = Model.parseObject(entity);
        if (ValidHelper.isNotEmpty(entity.getParentId())) {
            SysFunction parent = sysFunctionMapper.selectByPrimaryKey(id);
            if (ValidHelper.isNotEmpty(parent)) {
                model.set("parentName", parent.getName());
            }
        }
        return model;
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    public int delete(String ids) {
        SysFunction query = new SysFunction();
        query.setIdQueryIn(ids);
        query.setIsDeleted(1);
        query.setUpdateBy(UserHelper.getUserId());
        query.setUpdateTime(DateHelper.formatNow());
        return sysFunctionMapper.updateBySelective(query);
    }
}
