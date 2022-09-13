package com.wisely.sys.controller;

import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sys.entity.SysProject;
import com.wisely.sys.service.SysProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sysProject")
public class SysProjectController {

    @Resource
    SysProjectService sysProjectService;

    @RequestMapping("/list/api")
    @Converter(path = "project/list")
    public Object list(SysProject query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysProjectService.list(query, pageVo));
    }

}
