package com.wisely.ucenter.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.api.SysNetApi;
import com.wisely.ucenter.client.handler.UcDictHelper;
import com.wisely.ucenter.client.vo.UcenterRoleVo;
import com.wisely.ucenter.entity.UcenterPersonRole;
import com.wisely.ucenter.mapper.UcenterObjFuncMapper;
import com.wisely.ucenter.mapper.UcenterPersonRoleMapper;
import com.wisely.ucenter.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @since 2021-05-28 17:42:03
 */
@Service
public class MenuServiceImpl implements MenuService, SsoConstants {

    @Resource
    UcenterObjFuncMapper ucenterObjFuncMapper;
    @Resource
    UcenterPersonRoleMapper ucenterPersonRoleMapper;

    @Resource
    SysNetApi sysNetApi;


    /**
     * 将菜单数据转化为树结构 应用ID->ParentId->ID
     *
     * @param functions
     * @return
     */
    private Model<Integer, List<Model>> tree(List<Model> functions) {

        Model<Integer, List<Model>> projectModel = Model.builder();
        Model<Integer, List<Model>> modelMenu = Model.builder();

        // 处理菜单结构
        functions.forEach(func -> {
            // 获取
            if (func.isEmpty("parentId")) {
                projectModel.getList(func.getInt("projectId"), true).add(func);
            }

            modelMenu.getList(func.getInt("parentId"), true).add(func);
            func.set("subFunction", modelMenu.getList(func.getInt("id"), true));
        });
        return projectModel;
    }

    /**
     * 获取该当前用户的菜单
     *
     * @return
     */
    @Override
    public List<Model> userFunctionTree() {

        Model projectQuery = Model.builder();
        List<Model> projectList = sysNetApi.projectList(projectQuery);
        if (ValidHelper.isEmpty(projectList)) {
            return Lists.newArrayList();
        }

        // 获取用户所有菜单
        List<Model> functionList = this.loadFunctionByAuth(UserHelper.getPersonId());
        Model<Integer, List<Model>> menuModel = this.tree(functionList);


        List<Model> list = Lists.newArrayList();
        projectList.forEach(project -> {
            if (!menuModel.containsKey(project.getInt("id"))) {
                return;
            }
            list.add(Model.parseObject(project).set("subFunction", menuModel.get(project.getInt("id"))));
        });
        return list;
    }


    @Override
    public List<Model> loadFunctionByAuth(Integer personId) {
        Model functionQuery = Model.builder();


        // 存在Api接口调用，不能从会话信息中获取角色
        Optional<UcenterPersonRole> roleAdmin = Optional.empty();
        UcenterPersonRole personRoleQuery = new UcenterPersonRole();
        personRoleQuery.setIsDeleted(0);
        personRoleQuery.setPersonId(personId);
        List<UcenterPersonRole> personRoleList =
                ucenterPersonRoleMapper.selectListBySelective(personRoleQuery);
        if (ValidHelper.isNotEmpty(personRoleList)) {
            roleAdmin =
                personRoleList.stream()
                        .filter(personRole -> {
                            UcenterRoleVo roleVo = UcDictHelper.loadRoleVo(personRole.getRoleId());
                            return ValidHelper.isNotEmpty(roleVo) && StringHelper.equals(roleVo.getCode(), ROLE_SUPER_ADMIN);
                        }).findFirst();
        }

        if (!roleAdmin.isPresent()) {
            Set<Integer> funcIdSet = Sets.newHashSet(-1);
            List<Integer> objFuncIdList =
                    ucenterObjFuncMapper.selectFuncIdsByPersonId(Model.builder().set("personId", personId));
            funcIdSet.addAll(objFuncIdList);
            functionQuery.set("idQueryIn", StringHelper.join(funcIdSet, ","));
        }

        return sysNetApi.functionList(functionQuery);
    }
}
