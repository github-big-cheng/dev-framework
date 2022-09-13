package com.wisely.ucenter.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.api.SysNetApi;
import com.wisely.ucenter.mapper.UcenterObjFuncMapper;
import com.wisely.ucenter.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @since 2021-05-28 17:42:03
 */
@Service
public class MenuServiceImpl implements MenuService, SsoConstants {

    @Resource
    UcenterObjFuncMapper ucenterObjFuncMapper;

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
            Integer parentId = func.getInt("parentId", -1);
            if (func.isEmpty("parentId")) {
                projectModel.getList(func.getInt("projectId"), true).add(func);
            }

            modelMenu.getList(parentId, true).add(func);
            func.set("subFunction", modelMenu.getList(func.getInt("id"), true));
        });
        return projectModel;
    }


    /**
     * 获取包含应用的菜单树
     *
     * @param input
     * @return
     */
    @Override
    public List<Model> authFunctionTree(Model input) {

        Model projectQuery = Model.builder();
        List<Model> projectList = sysNetApi.projectList(projectQuery);
        if (ValidHelper.isEmpty(projectList)) {
            return Lists.newArrayList();
        }

        List<Model> functions = this.loadFunctionByAuth(UserHelper.getPersonId());
        Model<Integer, List<Model>> treeMenu = this.tree(functions);

        List<Model> list = Lists.newArrayList();
        projectList.forEach(project -> {
            list.add(project.set("subFunction", treeMenu.getList(project.getInt("id"), true)));

        });
        return list;
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
        if (ValidHelper.isEmpty(UserHelper.getUser()) || !UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            Set<Integer> funcIdSet = Sets.newHashSet(-1);
            List<Integer> objFuncIdList =
                    ucenterObjFuncMapper.selectFuncIdsByPersonId(Model.builder().set("personId", personId));
            funcIdSet.addAll(objFuncIdList);
            functionQuery.set("idQueryIn", StringHelper.join(funcIdSet));
        }

        return sysNetApi.functionList(functionQuery);
    }
}
