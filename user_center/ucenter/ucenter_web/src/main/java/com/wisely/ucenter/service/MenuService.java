package com.wisely.ucenter.service;

import com.wisely.framework.entity.Model;

import java.util.List;

/**
 * 功能(TUcenterFunction)表服务接口
 *
 * @author system
 * @since 2021-05-28 17:42:02
 */
public interface MenuService {

    /**
     * 获取当前用户的菜单
     *
     * @return
     */
    List<Model> userFunctionTree();

    /**
     * 根据人员ID获取人员菜单
     *
     * @param personId
     * @return
     */
    List<Model> loadFunctionByAuth(Integer personId);
}