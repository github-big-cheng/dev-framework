package com.wisely.ucenter.controller;


import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 菜单(TUcenterFunction)表控制层
 *
 * @author system
 * @since 2021-05-28 17:42:04
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    MenuService menuService;

    /**
     * 用户菜单树
     *
     * @return
     */
    @RequestMapping("/user/tree")
    @Converter(path = "menu/usertree")
    public Model list() {
        return ResponseBuilder.buildSuccess(menuService.userFunctionTree());
    }
}
