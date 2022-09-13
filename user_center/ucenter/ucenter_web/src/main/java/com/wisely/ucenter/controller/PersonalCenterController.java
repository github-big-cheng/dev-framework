package com.wisely.ucenter.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.encry.Md5Helper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterUser;
import com.wisely.ucenter.service.UcenterPersonService;
import com.wisely.ucenter.service.UcenterUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/personalCenter")
public class PersonalCenterController {


    @Resource
    private UcenterPersonService ucenterPersonService;

    @Resource
    private UcenterUserService ucenterUserService;


    /**
     * 修改头像
     *
     * @param ucenterPerson personId
     *                      password
     *                      personImg
     * @return
     */
    @RequestMapping("/updatePersonImg")
    @Converter(path = "personalCenter/updatePersonImg")
    public Model savePersonImg(UcenterPerson ucenterPerson) {
        ucenterPersonService.personImgSave(ucenterPerson);
        return ResponseBuilder.buildSuccess();
    }

    /**
     * 修改密码
     *
     * @param
     * @return
     */
    @RequestMapping("/updatePassword")
    @Converter(path = "personalCenter/updatePassword")
    public Model updatePassword() {

        Model input = RequestHelper.getInput();
        // 原密码校验
        UcenterUser user = ucenterUserService.loadByAccount(UserHelper.getAccount());
        AssertHelper.EX_BUSINESS.isNotEmpty(user, "personalCenter.user_not_found");

        String password = input.getString("password");
        String newPassword = input.getString("newPassword");

        String oldPassword = Md5Helper.encryptPwd(user.getAccount(), password, user.getSalt());
        AssertHelper.EX_BUSINESS.isEquals(oldPassword, user.getPassword(), "personalCenter.password_check_failed");
        // 修改新密码
        user.setFirstLogin(0);
        ucenterPersonService.updatePassword(user, newPassword);

        return ResponseBuilder.buildSuccess();
    }


}
