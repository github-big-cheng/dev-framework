package com.wisely.ucenter.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterObjFunc;
import com.wisely.ucenter.mapper.UcenterObjFuncMapper;
import com.wisely.ucenter.service.MenuService;
import com.wisely.ucenter.service.UcenterObjFuncService;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UcenterObjFuncServiceImpl implements UcenterObjFuncService {

    @Resource
    UcenterObjFuncMapper ucenterObjFuncMapper;

    @Resource
    MenuService menuService;

    /**
     * 获取权限树（有权限的选中）
     *
     * @param query
     * @return
     */
    @Override
    public Object list(UcenterObjFunc query) {
        Model input = RequestHelper.getInput();
        //所有的权限
        return menuService.authFunctionTree(input);
    }


    /**
     * 修改权限
     *
     * @return
     */
    @Override
    @Transactional
    public int save(UcenterObjFunc record) {

        Model input = RequestHelper.getInput();
        AssertHelper.EX_VALIDATION.isNotBlank(record.getObjType(), "common.parameter_required.objType");
        AssertHelper.EX_VALIDATION.isNotEmpty(record.getObjId(), "common.parameter_required.objId");

        //获取传入的应用对应所有权限的ID
        List<Model> functions = menuService.authFunctionTree(input);
        String functionIds = functions.stream().map(function -> DataHelper.getString(function.getInt("id"))).collect(Collectors.joining(","));
        // 删除该应用对应的所有权限
        record.setFuncIdQueryIn(functionIds);
        ucenterObjFuncMapper.deleteBySelective(record);

        // 添加
        if (input.isNotBlank("funcIds")) {
            List<String> funcIds = input.getSpitList("funcIds", ",");
            funcIds.stream().forEach(d -> {
                record.setOrgId(UserHelper.getOrgId());
                record.setOrgName(UserHelper.getOrgName());
                record.setCreateBy(UserHelper.getUserId());
                record.setCreateTime(DateHelper.formatNow());
                record.setFuncId(DataHelper.getInt(d));
                record.setIsDeleted(0);
                ucenterObjFuncMapper.insertSelective(record);
            });
            return funcIds.size();
        }
        return 0;
    }
}
