package com.wisely.sys.controller;

import com.wisely.sys.entity.SysOperateLog;
import com.wisely.sys.service.SysOperateLogService;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("sysOperationLog")
public class SysOperateLogController {

    @Resource
    SysOperateLogService sysOperateLogService;

    @RequestMapping("/list")
    @Converter(path = "sysoperationlog/list")
    public Model list(SysOperateLog query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysOperateLogService.findPage(query, pageVo));
    }

    @RequestMapping("/add/api")
    @Converter(path = "sysoperationlog/add")
    public Model add(SysOperateLog sysOperateLog) {
        return ResponseBuilder.buildSuccess(sysOperateLogService.add(sysOperateLog));
    }
}
