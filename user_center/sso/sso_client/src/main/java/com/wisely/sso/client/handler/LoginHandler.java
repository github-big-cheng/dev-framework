package com.wisely.sso.client.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wisely.framework.entity.FrameworkRequestWrapper;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.ValidationException;
import com.wisely.framework.exception.eum.ExceptionCodeEnum;
import com.wisely.framework.helper.*;
import com.wisely.framework.helper.encry.Md5Helper;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.filter.LoginFilterProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

import static com.wisely.sso.client.SsoConstants.*;

@Slf4j
public class LoginHandler {


    /**
     * 登录验证
     *
     * @param _sgk
     * @param osType
     * @return
     */
    public static SsoUser doCheck(String _sgk, String osType) {

        AssertHelper.EX_BUSINESS.isNotEmpty(_sgk, "common.parameter_required.{0}", SSO_KEY);
        AssertHelper.EX_BUSINESS.isNotEmpty(_sgk, "common.parameter_required.osType");

        // 检查是否保存登录票据信息
        String userJson = RedisHelper.get(TICKET_PREFIX + osType + _sgk);
        AssertHelper.EX_BUSINESS.isNotEmpty(userJson, "login.login_checked_failed");

        SsoUser ssoUser = JsonHelper.json2Obj(userJson, SsoUser.class);
        AssertHelper.EX_BUSINESS.isNotEmpty(ssoUser, "login.login_checked_failed");

        // 检查token是否为当前活动ticket
        String realTicket = RedisHelper.get(ACCOUNT_PREFIX + osType + ssoUser.getAccount());
        AssertHelper.EX_BUSINESS.isEquals(realTicket, _sgk, "login.login_time_out");

        // 续约
        RedisHelper.expire(TICKET_PREFIX + osType + _sgk, LOGIN_EXPIRATION_TIME);
        RedisHelper.expire(ACCOUNT_PREFIX + osType + ssoUser.getAccount(), LOGIN_EXPIRATION_TIME);

        return ssoUser;
    }


    /**
     * 签名验证
     *
     * @param input
     * @param wrapper
     */
    public static boolean sign(Model input, FrameworkRequestWrapper wrapper, long expiredTime) {
        //加密方式
        String signType = input.getString("signType", "MD5");
        //时间戳
//        AssertHelper.isEmpty(input, "signCode","login.signCode_is_not_empty");
//        AssertHelper.isEmpty(input, "timeStamp","login.timeStamp_is_not_empty");
        String signCode = input.getString("signCode", "");
        if (StringHelper.isBlank(signCode)) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "common.parameter_required.signCode", null);
        }
        String nonceStr = input.getString("nonceStr", "");
        if (StringHelper.isBlank(nonceStr)) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "common.parameter_required.nonceStr", null);
        }
        if (StringHelper.length(nonceStr) > 32) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "login.nonceStr_is_length_error", null);
        }
        long timeStamp = input.getLong("timeStamp");
        if (ValidHelper.isNull(input.getString("timeStamp"))) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "common.parameter_required.timeStamp", null);
            return false;
        }
        if (System.currentTimeMillis() - timeStamp > expiredTime) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "login.timeStamp_is_expired", null);
            return false;
        }
        //获取data参数
        Model paramMap = Model.builder();
        String data = input.getString("data");
        if (StringHelper.isNotEmpty(data)) {
            Map<String, Object> map = JsonHelper.json2Obj(data, new TypeReference<Map<String, Object>>() {
            });
            if (ValidHelper.isNotEmpty(map)) {
                paramMap = Model.builder();
                for (Map.Entry<String, Object> e : map.entrySet()) {
                    paramMap.put(e.getKey(), e.getValue());
                    wrapper.addParameter(e.getKey(), e.getValue());
                    input.set(e.getKey(), e.getValue());
                }
            }
        }
        if (ValidHelper.isNull(paramMap)) {
            RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "common.parameter_required.data", null);
            return false;
        }
        //排序后
        TreeMap<String, Object> treeMap = new TreeMap<>(paramMap);
        if (StringHelper.equalsIgnoreCase(signType, "MD5")) {
            String signature = Md5Helper.encryptMD5(JsonHelper.obj2Json(treeMap) + nonceStr);
            if (StringHelper.equalsIgnoreCase(signature, signCode)) {
                return true;
            } else {
                RequestHelper.writeResponse(ExceptionCodeEnum.VALIDATION.getCode(), "login.signature_verification_failed", null);
                return false;
            }
        }
        return false;
    }


    /**
     * 手机端验证
     * 含request转换
     *
     * @param input
     * @return
     */
    public static boolean doSign(Model input) {
        try {
            Integer expiredTime;
            if (SpringHelper.hasBean(LoginFilterProperties.class)) {
                expiredTime = SpringHelper.getBean(LoginFilterProperties.class).getExpiredTime();
            } else {
                expiredTime = new LoginFilterProperties().getExpiredTime();
            }

            FrameworkRequestWrapper requestWrapper = new FrameworkRequestWrapper(RequestHelper.getRequest());
            boolean signFlag =
                    LoginHandler.sign(input, requestWrapper, expiredTime);
            if (!signFlag) {
                throw ValidationException.builder("login.signature_verification_failed");
            }

//            input.clear();
            input.putAll(RequestHelper.getInput(requestWrapper, true));
        } catch (Exception e) {
            //throw SystemException.builder(e, "common.system_exception");
        }
        return true;
    }

}
