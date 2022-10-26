package com.wisely.ucenter.service.impl;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.api.SysNetApi;
import com.wisely.ucenter.entity.UcenterObjFunc;
import com.wisely.ucenter.mapper.UcenterObjFuncMapper;
import com.wisely.ucenter.service.UcenterObjFuncService;
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
    SysNetApi sysNetApi;

    /**
     * 获取权限树（有权限的选中）
     *
     * @param query
     * @return
     */
    @Override
    public Object list(UcenterObjFunc query) {

        Model input = RequestHelper.getInput();

        Model params = Model.builder();
        params.putByPickUp(input, "projectId");
        List<Model> functionList = sysNetApi.functionList(params);
        if (ValidHelper.isEmpty(functionList)) {
            return Lists.newArrayList();
        }

        // 当前查询条件有权限的菜单
        query.setIsDeleted(0);
        List<UcenterObjFunc> objFuncList = ucenterObjFuncMapper.selectListBySelective(query);
        Set<Integer> functionIdSet = objFuncList.stream().map(UcenterObjFunc::getFuncId).collect(Collectors.toSet());

        // 构造成树结构
        return this.tree(functionList, functionIdSet);
    }


    private List<Model> tree(List<Model> functionList, Set<Integer> functionIdSet) {

        List<Model> resultList = Lists.newArrayList();
        if (ValidHelper.isEmpty(functionList)) {
            return resultList;
        }

        Model menuModel = Model.builder();
        functionList.forEach(function -> {
            if (function.isEmpty("parentId")) {
                resultList.add(function);
            }

            if (functionIdSet.contains(function.getInt("id"))) {
                function.set("isSelected", 1);
            }

            menuModel.getList(function.getInt("parentId"), true).add(function);
            function.set("subFunction", menuModel.getList(function.getInt("id"), true));

        });

        return resultList;
    }

    /**
     * 修改权限
     *
     * @return
     */
    @Transactional
    @Override
    public int save(UcenterObjFunc record) {

        Model input = RequestHelper.getInput();

        // 按条件清空原记录
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
