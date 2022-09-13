package com.wisely.sys.controller;


import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sys.entity.SysFile;
import com.wisely.sys.service.SysFileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统文件
 *
 * @author ruijie.hu
 * @since 2021-06-03 18:16:33
 */
@RestController
@RequestMapping("/sysFile")
public class SysFileController {
    /**
     * 服务对象
     */
    @Resource
    SysFileService sysFileService;


    @RequestMapping("/list/api")
    @Converter(path = "sysFile/list")
    public Object list(SysFile sysFile) {
        return ResponseBuilder.buildSuccess(sysFileService.selectListBySelective(sysFile));
    }

    /**
     * 系统参数保存修改系统参数接口
     * 传参:code 页面代码
     */
    @RequestMapping("/add/api")
    @Converter(path = "sysFile/add")
    public Object save() {
        Model input = RequestHelper.getInput();
        return ResponseBuilder.buildSuccess(sysFileService.save(input));
    }


    /**
     * 根据fileType和sourceId删除对应的附件
     *
     * @return
     */
    @RequestMapping("/delete/api")
    @Converter(path = "sysFile/delete")
    public Model delete(SysFile sysFile) {
        return ResponseBuilder.buildSuccess(sysFileService.delete(sysFile));
    }

}
