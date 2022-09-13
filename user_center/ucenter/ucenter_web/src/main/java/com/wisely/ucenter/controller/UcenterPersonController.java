package com.wisely.ucenter.controller;


import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.service.UcenterPersonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 人员(UcenterPerson)表控制层
 *
 * @author xintao.li
 * @since 2021-05-31 10:52:02
 */
@RestController
@RequestMapping("ucenterPerson")
public class UcenterPersonController {
    /**
     * 服务对象
     */
    @Resource
    UcenterPersonService ucenterPersonService;

    /**
     * 分页查询所有人员数据
     *
     * @param pageVo        分页对象
     * @param ucenterPerson 查询实体
     * @return 所有数据
     */
    @RequestMapping(value = {"/list", "/list/combox", "/list/api"})
    @Converter(path = "person/list")
    public Model list(UcenterPerson ucenterPerson, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterPersonService.findByPage(ucenterPerson, pageVo));
    }


    @RequestMapping(value = {"/combox", "/combox/api"})
    @Converter(path = "person/combox")
    public Model combox(UcenterPerson ucenterPerson) {
        return ResponseBuilder.buildSuccess(ucenterPersonService.findList(ucenterPerson));
    }

    /**
     * 新增人员
     *
     * @param record UcenterPersonV
     * @return Model
     */
    @RequestMapping(value = {"/add", "/add/api"})
    @Converter(path = "person/add")
    public Model add(UcenterPerson record) {
        return ResponseBuilder.buildSuccess(ucenterPersonService.save(record));
    }

    /**
     * 编辑
     *
     * @param ucenterPerson UcenterPerson
     * @return Model
     */
    @RequestMapping(value = {"/save", "/save/api"})
    @Converter(path = "person/edit")
    public Model edit(UcenterPerson ucenterPerson) {
        return ResponseBuilder.buildSuccess(ucenterPersonService.save(ucenterPerson));
    }

    
    /**
     * 删除
     *
     * @param idQueryIn 多个personId 逗号分隔
     * @return Model
     */
    @RequestMapping(value = {"/delete", "/delete/api"})
    @Converter(path = "person/delete")
    public Model delete(String idQueryIn) {
        ucenterPersonService.delete(idQueryIn);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 查看
     *
     * @param id 主键id
     * @return Model
     */
    @RequestMapping("/view")
    @Converter(path = "person/view")
    public Model view(Integer id) {
        return ResponseBuilder.buildSuccess(ucenterPersonService.load(id));
    }


    /**
     * 重置密码
     *
     * @param account 人员账号
     * @return Model
     */
    @RequestMapping(value = {"/resetPwd", "/resetPwd/api"})
    @Converter(path = "person/resetpwd")
    public Model resetPwd(String account) {
        ucenterPersonService.resetPwd(account);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 锁定
     *
     * @param accounts 多个人员账号 逗号分隔
     * @return Model
     */
    @RequestMapping(value = {"/lock", "/lock/api"})
    @Converter(path = "person/lock")
    public Model lock(String accounts) {
        ucenterPersonService.accountOperation(accounts, 2);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 解锁
     *
     * @param accounts 多个人员账号 逗号分隔
     * @return Model
     */
    @RequestMapping(value = {"/unlock", "/unlock/api"})
    @Converter(path = "person/unlock")
    public Model unlock(String accounts) {
        ucenterPersonService.accountOperation(accounts, 1);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 注销
     *
     * @param accounts 多个人员账号 逗号分隔
     * @return Model
     */
    @RequestMapping(value = {"/destroy", "/destroy/api"})
    @Converter(path = "person/destroy")
    public Model destroy(String accounts) {
        ucenterPersonService.accountOperation(accounts, 3);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 获取指定角色的人员(如果有部门则获取指定部门指定角色的人员)
     *
     * @return
     */
    @RequestMapping(value = {"/loadPersonBySelective/api"})
    public Model loadPersonBySelective() {
        return ResponseBuilder.buildSuccess(ucenterPersonService.
                loadPersonBySelective(RequestHelper.getInput()));
    }

}
