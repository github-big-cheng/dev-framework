package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.common.cache.FunctionCache;
import com.wisely.sys.entity.SysFunction;
import com.wisely.sys.mapper.SysFunctionMapper;
import com.wisely.sys.service.SysFunctionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能(SysFunction)表服务实现类
 *
 * @author system
 * @since 2021-05-28 17:42:03
 */
@Service
public class SysFunctionServiceImpl implements SysFunctionService, SsoConstants {

    @Resource
    SysFunctionMapper sysFunctionMapper;

    @Resource
    FunctionCache functionCache;

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

        SysFunction query = new SysFunction();
        query.setCode(record.getCode());
        query.setIsDeleted(0);
        List<SysFunction> functionList = sysFunctionMapper.selectListBySelective(query);

        if (ValidHelper.isEmpty(record.getId())) {

            AssertHelper.EX_BUSINESS.isEmpty(functionList, "sys_function.repeat_code_found.{0}", record.getCode());

            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(DateHelper.formatNow());
            record.setIsDeleted(0);
            sysFunctionMapper.insert(record);
        } else {
            if (ValidHelper.isNotEmpty(functionList)) {
                functionList.forEach(function ->
                        AssertHelper.EX_BUSINESS.isEquals(
                                record.getId(),
                                function.getId(),
                                "sys_function.repeat_code_found.{0}", record.getCode()));
            }

            // 修改
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            sysFunctionMapper.updateByPrimaryKeySelective(record);
        }

        // 刷新缓存
        SysFunction cache = sysFunctionMapper.selectByPrimaryKey(record.getId());
        functionCache.syncCache(cache);
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

    @Override
    public List<Model> listTree(SysFunction query) {

        List<Model> list = Lists.newArrayList();

        query.setIsDeleted(0);
        List<SysFunction> functionList = sysFunctionMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(functionList)) {
            return list;
        }

        Model<Integer, List<Model>> temp = Model.builder();
        functionList.forEach(function -> {
            Model item = Model.parseObject(function);
            item.set("subFunction", temp.getList(function.getId(), true));
            temp.getList(function.getParentId(), true).add(Model.parseObject(function));
            if (ValidHelper.isEmpty(function.getParentId())) {
                list.add(item);
            }
        });

        return list;
    }
}
