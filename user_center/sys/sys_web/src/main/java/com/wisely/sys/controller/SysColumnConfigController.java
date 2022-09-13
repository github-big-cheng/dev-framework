package com.wisely.sys.controller;


import com.wisely.sys.entity.SysColumnConfig;
import com.wisely.sys.service.SysColumnConfigService;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义页面配置表(TSysColumnConfig)表控制层
 *
 * @author system
 * @since 2021-06-01 18:16:33
 */
@RestController
@RequestMapping("/sysColumnConfig")
public class SysColumnConfigController {
    /**
     * 服务对象
     */
    @Resource
    SysColumnConfigService sysColumnConfigService;

    /**
     * 用户配置列表查询
     * 传参:code 页面代码
     */
    @RequestMapping("/list")
    @Converter(path = "syscolumnconfig/list")
    public Object list() {
        Model request = RequestHelper.getInput();
        SysColumnConfig query = new SysColumnConfig();
        query.setCode(request.getString("code"));
        query.setIsDeleted(0);
        return ResponseBuilder.buildSuccess(sysColumnConfigService.customConfigList(query));
    }


    /**
     * 配置列表查询
     * 传参:code 页面代码
     */
    @RequestMapping("/fullList")
    @Converter(path = "syscolumnconfig/fullList")
    public Object fullList() {

        Model request = RequestHelper.getInput();

        SysColumnConfig query = new SysColumnConfig();
        query.setCode(request.getString("code"));
        query.setIsDeleted(0);
        return ResponseBuilder.buildSuccess(sysColumnConfigService.fullList(query));
    }

    /**
     * 用户页面配置保存
     * 传参: records 用户自定义配置列表
     */
    @RequestMapping("/save")
    @Converter(path = "syscolumnconfig/save")
    public Object save(String records) {
        List<SysColumnConfig> configList =
                JsonHelper.json2Obj(records, new TypeReference<List<SysColumnConfig>>() {});
        sysColumnConfigService.save(configList);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 还原默认配置
     * 传参: code   页面代码
     */
    @RequestMapping("/reset")
    @Converter(path = "syscolumnconfig/reset")
    public Object reset(SysColumnConfig record) {
        sysColumnConfigService.reset(record);
        return ResponseBuilder.buildSuccess();
    }


}
