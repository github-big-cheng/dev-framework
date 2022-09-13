package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.service.UcenterUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户相关
 *
 * @author ruijie.hu
 * @date 2021/6/2 13:49
 */
@RestController
@RequestMapping("ucenterUser")
public class UcenterUserController {

    @Resource
    UcenterUserService ucenterUserService;

    /**
     * 通过personId获取ssoModel
     *
     * @return Model
     */
    @RequestMapping("/loadUser/api")
    public Model loadUser() {
        return ResponseBuilder.buildSuccess(ucenterUserService.loadSsoUser());
    }

}
