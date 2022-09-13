package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.service.UcenterCacheService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 缓存刷新
 */
@RestController
@RequestMapping("/ucenterCache")
public class UcenterCacheController {


    @Resource
    UcenterCacheService ucenterCacheService;


    /**
     * 刷新缓存方法
     *
     * @return
     */
    @RequestMapping(value = {"/refresh", "/refresh/api"})
    @Converter(path = "cache/refresh")
    public Object refresh() {
        Model input = RequestHelper.getInput();
        ucenterCacheService.refreshByType(input.getString("type"));
        return ResponseBuilder.buildSuccess();
    }
}
