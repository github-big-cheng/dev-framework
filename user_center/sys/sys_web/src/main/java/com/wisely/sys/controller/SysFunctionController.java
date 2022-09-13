package com.wisely.sys.controller;


import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sys.entity.SysFunction;
import com.wisely.sys.service.SysFunctionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 菜单(TSysFunction)表控制层
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:42:04
 */
@RestController
@RequestMapping("sysFunction")
public class SysFunctionController {

    @Resource
    SysFunctionService sysFunctionService;


    /**
     * 分页列表
     *
     * @param query
     * @param pageVo
     * @return
     */
    @RequestMapping(value = {"/list", "/list/api"})
    @Converter(path = "function/list")
    public Object list(SysFunction query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(sysFunctionService.findByPage(query, pageVo));
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    @Converter(path = "function/add")
    public Object add(SysFunction record) {
        return ResponseBuilder.buildSuccess(sysFunctionService.save(record));
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    @RequestMapping("/save")
    @Converter(path = "function/save")
    public Object save(SysFunction record) {
        return ResponseBuilder.buildSuccess(sysFunctionService.save(record));
    }

    /**
     * 单条查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/view")
    @Converter(path = "function/view")
    public Object view(Integer id) {
        AssertHelper.EX_VALIDATION.isNotEmpty(id, "common.parameter_required.id");
        return ResponseBuilder.buildSuccess(sysFunctionService.view(id));
    }


    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    @Converter(path = "function/delete")
    public Object delete() {
        Model input = RequestHelper.getInput();
        AssertHelper.EX_VALIDATION.isNotEmpty(input, "idQueryIn", "common.parameter_required.idQueryIn");
        return ResponseBuilder.buildSuccess(sysFunctionService.delete(input.getString("idQueryIn")));
    }
}
