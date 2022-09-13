package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterPersonOrg;
import com.wisely.ucenter.service.UcenterPersonOrgService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.wisely.sso.client.SsoConstants.ROLE_SUPER_ADMIN;

@RequestMapping("/ucenterDeptPerson")
@RestController
public class UcenterPersonOrgController {

    @Resource
    UcenterPersonOrgService ucenterPersonOrgService;

    @RequestMapping(value = "/list")
    @Converter(path = "personOrg/list")
    public Model list(UcenterPersonOrg query, PageVo pageVo) {
        //不是管理员，过滤机构
        if (!UserHelper.hasRole(ROLE_SUPER_ADMIN)) {
            query.setRootOrgIdQueryIn(UserHelper.getOrganizations());
        }
        return ResponseBuilder.buildSuccess(ucenterPersonOrgService.findByPage(query, pageVo));
    }

    @RequestMapping(value = "/list/api")
    @Converter(path = "personOrg/list")
    public Model listApi(UcenterPersonOrg query, PageVo pageVo) {
        return ResponseBuilder.buildSuccess(ucenterPersonOrgService.findByPage(query, pageVo));
    }

    @RequestMapping("/add")
    @Converter(path = "personOrg/add")
    public Model save() {
        return ResponseBuilder.buildSuccess(ucenterPersonOrgService.add(RequestHelper.getInput()));
    }


    @RequestMapping("/view")
    @Converter(path = "personOrg/view")
    public Model view(Integer personId) {
        return ResponseBuilder.buildSuccess(ucenterPersonOrgService.view(personId));
    }

}
