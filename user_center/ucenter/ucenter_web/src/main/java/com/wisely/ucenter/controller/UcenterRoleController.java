package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterRole;
import com.wisely.ucenter.service.UcenterRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 角色管理层
 *
 * @author xi.wang
 * @date 2021/5/31 18:49
 */
@RequestMapping("/ucenterRole")
@RestController
public class UcenterRoleController {


    @Resource
    UcenterRoleService ucenterRoleService;

    /**
     * 分页查询所有信息
     *
     * @param query
     * @param pageVo
     * @return
     */
    @RequestMapping(value = {"/list", "/combox"})
    @Converter(path = "role/list")
    public Object list(UcenterRole query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.findByPage(query, pageVo));
    }

    /**
     * 单条记录查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/view")
    @Converter(path = "role/view")
    public Object load(Integer id) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.load(id));
    }

    /**
     * 新增角色
     *
     * @param ucenterRole
     * @return
     */
    @RequestMapping("/add")
    @Converter(path = "role/add")
    public Object add(UcenterRole ucenterRole) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.save(ucenterRole));
    }

    /**
     * 编辑角色
     *
     * @return
     */
    @RequestMapping("/save")
    @Converter(path = "role/edit")
    public Object save(UcenterRole ucenterRole) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.save(ucenterRole));
    }


    /**
     * 删除角色
     *
     * @param idQueryIn
     * @return
     */
    @RequestMapping("/delete")
    @Converter(path = "role/delete")
    public Object delete(String idQueryIn) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.delete(idQueryIn));
    }

    /**
     * 获取所有角色信息(供platform同步人员数据时候,根据角色code查询到角色id)
     * @param ucenterRole
     * @return
     */
    @RequestMapping("/list/api")
    public Model selectAll(UcenterRole ucenterRole) {
        return ResponseBuilder.buildSuccess(ucenterRoleService.findList(ucenterRole));
    }
}
