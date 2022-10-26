package com.wisely.ucenter.controller;


import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.service.UcenterOrgService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 机构/部门信息(UcenterOrg)表控制层
 *
 * @author system
 * @since 2021-05-31 17:48:28
 */
@RestController
@RequestMapping("/ucenterOrg")
public class UcenterOrgController {

    /**
     * 服务对象
     */
    @Resource
    UcenterOrgService ucenterOrgService;


    /**
     * 列表显示
     *
     * @param query UcenterOrg
     * @return Model
     */
    @RequestMapping(value = {"/list", "/combox", "/page/api"})
    @Converter(path = "org/list")
    public Model list(UcenterOrg query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.findByPage(query, pageVo));
    }

    /**
     * 下拉框显示部门或者机构
     *
     * @param query
     * @return
     */
    @RequestMapping(value = {"/list/api"})
    @Converter(path = "org/combox")
    public Model combox(UcenterOrg query) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.findList(query));
    }

    /**
     * 新增
     *
     * @param record
     * @return Model
     */
    @RequestMapping(value = {"/add"})
    @Converter(path = "org/add")
    public Model add(UcenterOrg record) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.save(record));
    }

    /**
     * 编辑
     *
     * @param record UcenterOrg
     * @return Model
     */
    @RequestMapping(value = {"/save"})
    @Converter(path = "org/save")
    public Model edit(UcenterOrg record) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.save(record));
    }

    /**
     * 删除
     *
     * @param idQueryIn 批量部门id
     * @return Model
     */
    @RequestMapping(value = {"/delete"})
    @Converter(path = "org/delete")
    public Model delete(String idQueryIn) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.delete(idQueryIn));
    }

    /**
     * 查看部门
     *
     * @param id id
     * @return Model
     */
    @RequestMapping(value = {"/view"})
    @Converter(path = "org/view")
    public Model view(Integer id) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.load(id));
    }

    /**
     * 显示树状部门
     *
     * @param query
     * @return
     */
    @RequestMapping("/tree")
    @Converter(path = "org/tree")
    public Model tree(UcenterOrg query) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.tree(query));
    }

    /**
     * 部门人员
     *
     * @param query
     * @return
     */
    @RequestMapping("/listDeptPerson/tree")
    @Converter(path = "org/deptperson")
    public Model listDeptPerson(UcenterOrg query) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.deptPersonTree(query));
    }

    /**
     * 部门职位
     *
     * @param query
     * @return
     */
    @RequestMapping("/listDeptPosition/tree")
    @Converter(path = "org/deptposition")
    public Model listDeptPosition(UcenterOrg query) {
        return ResponseBuilder.buildSuccess(ucenterOrgService.deptPositionTree(query));
    }
}
