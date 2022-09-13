package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.entity.SysCode;
import com.wisely.sys.mapper.SysCodeMapper;
import com.wisely.sys.service.SysCodeService;
import com.wisely.sys.common.cache.CodeCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class SysCodeServiceImpl implements SysCodeService {

    @Resource
    SysCodeMapper sysCodeMapper;

    @Resource
    CodeCache codeCache;


    @Override
    public List<SysCode> findList(SysCode query) {
        query.setIsDeleted(0);
        return sysCodeMapper.selectListBySelective(query);
    }

    @Override
    public PageInfo<SysCode> findPage(SysCode ucenterCode, PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        ucenterCode.setIsDeleted(0);
        return new PageInfo<>(sysCodeMapper.selectListBySelective(ucenterCode));
    }

    @Override
    public String add(SysCode record) {
        record.setCreateBy(UserHelper.getUserId());
        record.setCreateTime(DateHelper.formatNow());
        record.setIsDeleted(0);
        sysCodeMapper.insertSelective(record);
        // 刷新缓存
        codeCache.syncCache(record);
        return record.getValue();
    }


    @Transactional
    @Override
    public String save(SysCode record) {
        //1.首先判断是否是系统创建OR用户创建,系统创建则抛出异常无法修改
        SysCode query = new SysCode();
        query.setIsDeleted(0);
        query.setValue(record.getValue());
        List<SysCode> codes = sysCodeMapper.selectListBySelective(query);
        codes.forEach(code ->
            AssertHelper.EX_BUSINESS.isNotEquals(code.getIsSys(), 1,
                    "sysCode.value_is_system.{0}", code.getValue()));

        //将updateByPrimaryKey方法中的创建人删除
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        record.setIsDeleted(0);
        sysCodeMapper.updateByPrimaryKey(record);

        // 刷新缓存
        codeCache.syncCache(record);
        return record.getValue();
    }

    @Override
    public SysCode load(String value) {
        return sysCodeMapper.selectByPrimaryKey(value);
    }

    @Override
    public int deleteByValues(String values) {

        //首先判断代码是否是系统初始化,系统初始化则无法进行删除
        SysCode query = new SysCode();
        query.setIsDeleted(0);
        query.setValueQueryIn(values);
        List<SysCode> codes = sysCodeMapper.selectListBySelective(query);
        if (ValidHelper.isNotEmpty(codes)) {
            codes.forEach(code ->
                    AssertHelper.EX_BUSINESS.isNotEquals(code.getIsSys(), 1,
                            "sysCode.value_is_system_not_deleted.{0}", code.getValue()));
        }

        try {
            //2.非系统初始化代码 则可以进行删除
            SysCode record = new SysCode();
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            record.setValueQueryIn(values);
            record.setIsDeleted(1);
            return sysCodeMapper.updateBySelective(record);
        } finally {
            if (ValidHelper.isNotEmpty(codes)) {
                codeCache.invalidate(codes.toArray(new SysCode[]{}));
            }
        }
    }

    @Override
    public List<Model> tree(SysCode query) {

        query.setIsDeleted(0);
        List<SysCode> codeList = sysCodeMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(codeList)) {
            return Lists.newArrayList();
        }

        AtomicReference<String> rootId = new AtomicReference<>(null);
        Model<String, List<Model>> codeModel = Model.builder();
        codeList.forEach(code -> {
            Model temp = Model.parseObject(code);
            codeModel.getList(code.getParent(), true).add(temp);
            temp.set("children", codeModel.getList(code.getValue(), true));

            // 省市县查询按pathValue获取根节点
            if (ValidHelper.isNotBlank(query.getPathValueQueryLike()) &&
                    ValidHelper.isEquals(code.getValue(), query.getPathValueQueryLike())) {
                rootId.set(code.getParent());
            }
        });
        if (ValidHelper.isNotBlank(query.getType())) {
            rootId.set(query.getType());
        }
        return codeModel.getModelList(rootId.get());
    }

}
