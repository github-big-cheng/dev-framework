package com.wisely.ucenter.service;

import com.wisely.framework.entity.Model;

import java.util.List;

/**
 * 功能(TUcenterFunction)表服务接口
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:42:02
 */
public interface MenuService {


    /**
     * 获取应用菜单树
     *
     * @param input
     * @return
     */
    List<Model> authFunctionTree(Model input);

    /**
     * 获取当前用户的菜单
     *
     * @return
     */
    List<Model> userFunctionTree();

    List<Model> loadFunctionByAuth(Integer personId);
}