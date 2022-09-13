package com.wisely.sso.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BusinessException;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.*;
import com.wisely.framework.helper.encry.Md5Helper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.entity.UcenterUser;
import com.wisely.sso.service.LoginService;
import com.wisely.ucenter.client.api.UcenterNetApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class LoginController implements SsoConstants {


    @Resource
    LoginService loginService;

    @Resource
    UcenterNetApi ucenterNetApi;

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("/login")
    @Converter(path = "user/login")
    public Object login() {

        Model model = RequestHelper.getInput();
        log.trace("request is ...{}", model);

        AssertHelper.EX_VALIDATION.isNotBlank(model, "username", "login.username_is_required", "username");
        AssertHelper.EX_VALIDATION.isNotBlank(model, "password", "login.password_is_required", "password");

        // 图形验证码
        String serviceVerifyCode = RedisHelper.get(VERIFY_IMAGE_PREFIX + RequestHelper.getSessionId());
        if (StringHelper.isNotBlank(serviceVerifyCode)) {
            AssertHelper.EX_BUSINESS.isNotBlank(model, "verifyCode", "login.verifyCode_is_required", "verifyCode");
            Integer verifyCode = model.getInt("verifyCode");
            // 校验图形验证码是否在合理区域
            AssertHelper.EX_BUSINESS.isTrue(
                    ValidHelper.isBetween(serviceVerifyCode, verifyCode - 1, verifyCode + 1),
                    "login.verify_code_is_invalid");
        }

        // 账号校验
        String account = model.getString("username");
        UcenterUser user = loginService.findUserByAccount(account);
        AssertHelper.EX_VALIDATION.isNotEmpty(user, "login.account_not_found.{0}", account);

        int opStat = 0;
        String osType = model.getString(OS_KEY, OS_PC);
        SsoUser ssoUser = null;
        try {
            // 用户状态校验
            opStat = 2;
            AssertHelper.EX_BUSINESS.isNotEquals(user.getStatus(), 2, "login.account_is_locked.{0}", account);
            opStat = 3;
            AssertHelper.EX_BUSINESS.isNotEquals(user.getStatus(), 3, "login.account_is_closed.{0}", account);

            // 密码校验
            String password = Md5Helper.encryptPwd(account, model.getString("password"), user.getSalt());
            if (!StringHelper.equalsIgnoreCase(password, user.getPassword())) {
                opStat = 5;
                // 登录失败最大次数校验
                Integer userFailCount = DataHelper.getInt(user.getIsErrorTime(), 0) + 1;
                Integer maxFailCount = ConfigHelper.getInt("login.max_failed_count", 6);
                if (userFailCount >= maxFailCount) {
                    user.setStatus(2); // 冻结
                }
                // 记录密码错误次数
                loginService.doLogin(user, false);
                Integer leftCount = maxFailCount - userFailCount;
                String message;
                if (leftCount <= 0) {
                    message = "login.account_is_locked";
                } else {
                    message = "login.login_nearly_locked.{0}";
                }
                throw BusinessException.builder(message, leftCount);
            }

            Model ssoUserModel = ucenterNetApi.loadSsoUser(user.getPersonId());
            AssertHelper.EX_BUSINESS.isNotEmpty(ssoUserModel, "login.person_info_load_failed");
            ssoUser = (SsoUser) ssoUserModel.convertTo(SsoUser.class);

            // 手机端校验IMEI IMSI
            if (StringHelper.equalsIgnoreCase(osType, OS_ANDROID)) {
                Model personInfo = ssoUser.getExtendedProperties();
                if (StringHelper.isNotBlank(personInfo.getString("imeiNo"))) {
                    AssertHelper.EX_BUSINESS.isEquals(personInfo.getString("imeiNo"), model.getString("imeiNo"), "login.client_id_check_failed");
                }
                if (StringHelper.isNotBlank(personInfo.getString("imsiNo"))) {
                    AssertHelper.EX_BUSINESS.isEquals(personInfo.getString("imsiNo"), model.getString("imsiNo"), "login.client_id_check_failed");
                }
            }

            // 设置登录票据
            String sgk = RandomHelper.uuid();
            ssoUser.setTicket(sgk);

            // 保存ticket 与用户的关系映射
            RedisHelper.set(TICKET_PREFIX + osType + sgk, JsonHelper.obj2Json(ssoUser), LOGIN_EXPIRATION_TIME);
            // 保存用户登录的ticket信息
            RedisHelper.set(ACCOUNT_PREFIX + osType + user.getAccount(), sgk, LOGIN_EXPIRATION_TIME);

            // 更新登录状态

            loginService.doLogin(user, true);
            opStat = 1;
        } finally {
            // 记录登录日志
            loginService.loginLogSave(ssoUser, osType, account, opStat);
        }

        return ResponseBuilder.buildSuccess(ssoUser);
    }


    /**
     * 登录验证
     *
     * @return
     */
    @RequestMapping("/checkTicket")
    @Converter(path = "user/check")
    public Object checkTicket() {
        return ResponseBuilder.buildSuccess(loginService.doCheck(RequestHelper.getInput()));
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/loginOut")
    @Converter(path = "user/out")
    public Object loginOut(@RequestHeader(value = SSO_KEY, required = false) String ticket) {

        Model model = RequestHelper.getInput();
        // 请求头获取失败，尝试请求体获取
        if (ValidHelper.isBlank(ticket)) {
            ticket = model.getString(SSO_KEY);
        }
        AssertHelper.EX_VALIDATION.isNotBlank(ticket, "common.parameter_required.{0}", SSO_KEY);

        String osType = model.getString(OS_KEY, OS_PC);
        String userJson = RedisHelper.get(TICKET_PREFIX + ticket);
        RedisHelper.del(TICKET_PREFIX + osType + ticket);

        // 移除用户的ticket记录
        UcenterUser ucenterUser = JsonHelper.json2Obj(userJson, UcenterUser.class);
        if (ucenterUser != null) {
            RedisHelper.del(ACCOUNT_PREFIX + osType + ucenterUser.getAccount());
        }

        return ResponseBuilder.buildSuccess();
    }


    /**
     * 免密登录接口
     *
     * @return
     */
    @RequestMapping("/redirectLogin")
    @Converter(path = "user/redirectLogin")
    public Model freeLogin() {
        return ResponseBuilder.buildSuccess(loginService.redirectLogin());
    }
}
