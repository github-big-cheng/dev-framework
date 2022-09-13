package com.wisely.sys.controller;


import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sys.entity.SysParameter;
import com.wisely.sys.service.SysParameterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统参数
 *
 * @author ruijie.hu
 * @since 2021-06-03 18:16:33
 */
@RestController
@RequestMapping("/sysParameter")
public class SysParameterController {
    /**
     * 服务对象
     */
    @Resource
    SysParameterService sysParameterService;


    /**
     * 系统运行参数
     *
     * @param sysParameter SysParameter对象
     * @param pageVo       PageVo对象
     * @return Object对象
     */
    @RequestMapping("/list")
    @Converter(path = "parameter/list")
    public Object list(SysParameter sysParameter, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysParameterService.findPage(sysParameter, pageVo));
    }

    /**
     * 其它参数
     * 传参:code 页面代码
     */
    @RequestMapping("/combox")
    @Converter(path = "parameter/combox")
    public Object list(SysParameter query) {
        return ResponseBuilder.buildSuccess(sysParameterService.list(query));
    }

    /**
     * 单个系统参数查询
     * 传参:code 页面代码
     */
    @RequestMapping(value = {"/load", "/load/api"})
    @Converter(path = "parameter/load")
    public Object load() {
        return ResponseBuilder.buildSuccess(sysParameterService.load());
    }


    /**
     * 系统参数保存修改系统参数接口
     * 传参:code 页面代码
     */
    @RequestMapping("/save")
    @Converter(path = "parameter/save")
    public Object save() {
        return ResponseBuilder.buildSuccess(sysParameterService.save());
    }


    /**
     * 刷新系统参数缓存
     * 传参: code   页面代码
     *
     * @param record
     * @return
     */
    @RequestMapping(value = {"/refresh/api", "/refresh"})
    @Converter(path = "parameter/refresh")
    public Object reset(SysParameter record) {
        sysParameterService.refresh(record.getOrgId());
        return ResponseBuilder.buildSuccess();
    }
}
