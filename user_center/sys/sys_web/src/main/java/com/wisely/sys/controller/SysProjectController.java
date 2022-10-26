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

    @RequestMapping({"/list", "/list/combox", "/list/api"})
    @Converter(path = "project/list")
    public Object list(SysProject query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysProjectService.list(query, pageVo));
    }


    @RequestMapping("/add")
    @Converter(path = "project/add")
    public Object add(SysProject record) {
        return ResponseBuilder.buildSuccess(sysProjectService.save(record));
    }


    @RequestMapping("/save")
    @Converter(path = "project/save")
    public Object save(SysProject record) {
        return ResponseBuilder.buildSuccess(sysProjectService.save(record));
    }


    @RequestMapping("/delete")
    @Converter(path = "project/delete")
    public Object delete(String idQueryIn) {
        return ResponseBuilder.buildSuccess(sysProjectService.delete(idQueryIn));
    }


    @RequestMapping("/view")
    @Converter(path = "project/view")
    public Object view(Integer id) {
        return ResponseBuilder.buildSuccess(sysProjectService.load(id));
    }


}
