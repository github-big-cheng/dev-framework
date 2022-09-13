package com.wisely.sso.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.handler.LoginHandler;
import com.wisely.sso.common.QrCodeHelper;
import com.wisely.sso.entity.UcenterUser;
import com.wisely.sso.service.LoginService;
import com.wisely.ucenter.client.api.UcenterNetApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;

/**
 * 二维码相关接口
 */
@RestController
@RequestMapping("/qrcode")
@Slf4j
public class QrCodeController implements SsoConstants {

    private final String CODE_PREFIX = "QR_CODE:";

    @Value("${qrCode.width}")
    private int width;

    @Value("${qrCode.height}")
    private int height;

    @Value("${qrCode.invalidTime}")
    private int invalidTime;


    @Resource
    LoginService loginService;

    @Resource
    UcenterNetApi ucenterNetApi;


    /**
     * PC端获取二维码
     */
    @RequestMapping("/generate")
    public Object generate() {

        Model rst = Model.builder();
        String text = RandomHelper.uuid();
        rst.set("qrCode", text);
        try {
            // 保存对应的二维码信息
            RedisHelper.set(CODE_PREFIX + text, "0", invalidTime); // 状态-待扫描,有效期2分钟

            byte[] imageBytes = QrCodeHelper.getQRImageBytes(text, width, height);
            rst.set("qrImage", new BASE64Encoder().encodeBuffer(imageBytes).trim().replaceAll("\n", "").replaceAll("\r", ""));

        } catch (Exception e) {
            log.error("generate error:{}", e);
        }
        return ResponseBuilder.buildSuccess(rst);
    }


    @Converter(path = "qrcode/scan", response = false)
    @RequestMapping("/scan")
    public Object scan(@RequestHeader(value = SSO_KEY, required = false) String ticket) {

        Model input = RequestHelper.getInput();

        String osType = input.getString(OS_KEY, OS_PC);
        if (StringHelper.equalsIgnoreCase(osType, OS_ANDROID)) {
            LoginHandler.doSign(input); // 手机端签名验签
        }

        // 请求头获取失败，尝试请求体获取
        if (ValidHelper.isBlank(ticket)) {
            ticket = input.getString(SSO_KEY);
        }


        // 参数校验
        AssertHelper.EX_VALIDATION.isNotBlank(ticket, "common.parameter_required._sgk");
        AssertHelper.EX_VALIDATION.isNotBlank(input, "qrCode", "common.parameter_required.qrCode");
        AssertHelper.EX_VALIDATION.isNotBlank(input, "opType", "common.parameter_required.qrCode");
        // 检查登录
        LoginHandler.doCheck(ticket, osType);

        String qrCode = input.getString("qrCode");

        // 检查二维码是否已失效
        String status = RedisHelper.get(CODE_PREFIX + qrCode);
        AssertHelper.EX_VALIDATION.isNotBlank(status, "qrCode_scan.invalid_code");

        // 检查websocket连接
        QrCodeWebSocket webSocket = QrCodeWebSocket.WEB_SOCKET_MAP.get(qrCode);
        AssertHelper.EX_BUSINESS.isNotEmpty(webSocket, "qrCode_scan.websocket_connect_not_found");

        // 通知PC客户端已扫描,待确认
        String opType = input.getString("opType");
        webSocket.sendMessage(JsonHelper.obj2Json(Model.builder().set("status", opType)));
        RedisHelper.set(CODE_PREFIX + qrCode, opType, invalidTime); // 重置状态，有效时间2分钟

        return ResponseBuilder.buildSuccess();
    }


    @RequestMapping("/login")
    public Object login(@RequestHeader(value = SSO_KEY, required = false) String ticket) {
        Model input = RequestHelper.getInput();

        String osType = input.getString(OS_KEY, OS_PC);
        if (StringHelper.equalsIgnoreCase(osType, OS_ANDROID)) {
            LoginHandler.doSign(input); // 手机端签名验签
        }

        // 请求头获取失败，尝试请求体获取
        if (ValidHelper.isBlank(ticket)) {
            ticket = input.getString(SSO_KEY);
        }

        // 参数校验
        AssertHelper.EX_VALIDATION.isNotBlank(ticket, "common.parameter_required._sgk");
        AssertHelper.EX_VALIDATION.isNotBlank(input, "qrCode", "common.parameter_required.qrCode");
        // 检查登录
        LoginHandler.doCheck(ticket, osType);

        // 检查确认状态
        String qrCode = RedisHelper.get(CODE_PREFIX + input.getString("qrCode"));
        AssertHelper.EX_VALIDATION.isEquals(qrCode, "1", "qrCode_login.login_confirm_not_yet");

        // 获取用户
        String userJson = RedisHelper.get(TICKET_PREFIX + osType + ticket);
        SsoUser ssoUser = JsonHelper.json2Obj(userJson, SsoUser.class);

        // 检查websocket状态
        QrCodeWebSocket webSocket = QrCodeWebSocket.WEB_SOCKET_MAP.get(input.getString("qrCode"));
        AssertHelper.EX_VALIDATION.isNotEmpty(webSocket, "qrCode_scan.websocket_connect_not_found");


        UcenterUser user = loginService.findUserByAccount(ssoUser.getAccount());
        AssertHelper.EX_VALIDATION.isNotEmpty(user, "qrCode_login.user_check_failed");

        int opStat = 0;
        try {
            // 用户状态校验
            opStat = 2;
            AssertHelper.EX_BUSINESS.isNotEquals(user.getStatus(), 2, "login.account_is_locked.{0}", ssoUser.getAccount());
            opStat = 3;
            AssertHelper.EX_BUSINESS.isNotEquals(user.getStatus(), 3, "login.account_is_closed.{0}", ssoUser.getAccount());

            // 登录成功
            Model ssoUserModel = ucenterNetApi.loadSsoUser(user.getPersonId());
            AssertHelper.EX_BUSINESS.isNotEmpty(ssoUserModel, "login.person_info_load_failed");
            ssoUser = (SsoUser) ssoUserModel.convertTo(SsoUser.class);

            // 设置登录票据
            String sgk = RandomHelper.uuid();
            ssoUser.setTicket(sgk);

            // 保存ticket 与用户的关系映射
            RedisHelper.set(TICKET_PREFIX + OS_PC + sgk, JsonHelper.obj2Json(ssoUser), LOGIN_EXPIRATION_TIME);
            // 保存用户登录的ticket信息
            RedisHelper.set(ACCOUNT_PREFIX + OS_PC + user.getAccount(), sgk, LOGIN_EXPIRATION_TIME);

            // 推送登录消息
            webSocket.sendMessage(JsonHelper.obj2Json(Model.builder().set("user", ssoUser).set("status", "2")));

            RedisHelper.del(qrCode);

            // 更新登录状态
            loginService.doLogin(user, true);

            opStat = 1;
        } finally {
            // 记录登录日志
            loginService.loginLogSave(ssoUser, "QRCODE", ssoUser.getAccount(), opStat);
        }

        return ResponseBuilder.buildSuccess();
    }

}
