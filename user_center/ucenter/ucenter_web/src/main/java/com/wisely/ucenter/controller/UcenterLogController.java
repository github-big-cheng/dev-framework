package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterLog;
import com.wisely.ucenter.service.UcenterLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("ucenterLog")
public class UcenterLogController {

    @Resource
    UcenterLogService ucenterLogService;


    @Converter(path = "/ucenterlog/list")
    @RequestMapping(value = "/list")
    public Model list(UcenterLog query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterLogService.findPage(query, pageVo));
    }
}
