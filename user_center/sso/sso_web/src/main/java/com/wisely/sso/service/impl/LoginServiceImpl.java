package com.wisely.sso.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.*;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.handler.LocalLoginCheck;
import com.wisely.sso.entity.UcenterLog;
import com.wisely.sso.entity.UcenterUser;
import com.wisely.sso.mapper.UcenterLogMapper;
import com.wisely.sso.mapper.UcenterUserMapper;
import com.wisely.sso.service.LoginService;
import com.wisely.ucenter.client.api.UcenterNetApi;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService, SsoConstants {


    @Resource
    UcenterUserMapper ucenterUserMapper;
    @Resource
    UcenterLogMapper ucenterLogMapper;


    @Resource
    UcenterNetApi ucenterNetApi;
    @Resource
    LocalLoginCheck localLoginCheck;


    /**
     * 用户条件查询
     *
     * @param query
     * @return
     */
    @Override
    public List<UcenterUser> findUserByEntity(UcenterUser query) {
        return ucenterUserMapper.selectListBySelective(query);
    }


    /**
     * 根据账号查找用户
     *
     * @param account
     * @return
     */
    @Override
    public UcenterUser findUserByAccount(String account) {
        UcenterUser query = new UcenterUser();
        query.setAccount(account);
        query.setIsDeleted(0);
        List<UcenterUser> users = findUserByEntity(query);
        if (ValidHelper.isEmpty(users)) {
            return null;
        }

        return users.get(0);
    }


    /**
     * 更新登录状态
     *
     * @param user
     */
    @Override
    public void doLogin(UcenterUser user, boolean success) {

        AssertHelper.EX_VALIDATION.isNotEmpty(user, "common.parameter_required.user");
        AssertHelper.EX_VALIDATION.isNotEmpty(user.getId(), "common.parameter_required.userId");

        UcenterUser record = new UcenterUser();
        record.setId(user.getId());
        if (!success) {
            if (DataHelper.getInt(user.getIsErrorTime()) >= 5) {
                record.setStatus(2); // 冻结
            }
            record.setIsErrorTime(DataHelper.getInt(user.getIsErrorTime(), 0) + 1);
        } else {
            record.setIsErrorTime(0);
        }
        record.setUpdateBy(user.getId());
        record.setUpdateTime(DateHelper.formatNow());
        ucenterUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void loginLogSave(@Nullable SsoUser ssoUser, String osType, String account, int opStat) {
        String now = DateHelper.formatNow();
        UcenterLog record = new UcenterLog();
        record.setOrgId(ssoUser == null ? null : ssoUser.getOrgId());
        record.setOrgName(ssoUser == null ? null : ssoUser.getOrgName());
        record.setLoginTime(now);
        record.setType("10078-10"); // 登录
        if (StringHelper.equalsIgnoreCase(osType, OS_ANDROID)) {
            osType = "手机";
        } else if (StringHelper.equalsIgnoreCase(osType, OS_PC)) {
            osType = "电脑";
        } else if (StringHelper.equalsIgnoreCase(osType, "QRCODE")) {
            osType = "二维码";
        }
        record.setOpContent("账号" + account + "使用" + osType + "方式" + "在" + RequestHelper.getIP() + "登录系统，" + (opStat == 1 ? "登录成功" : "登录失败"));
        record.setIp(RequestHelper.getIP());
        record.setUserId(ssoUser == null ? null : ssoUser.getId()); // 可以为null
        record.setOpState(opStat); // 1-成功
        record.setIsDeleted(0);
        record.setCreateBy(DataHelper.getString(1));
        record.setCreateTime(now);
        ucenterLogMapper.insertSelective(record);
    }

    @Override
    public SsoUser redirectLogin() {

        Model input = RequestHelper.getInput();
        String token = input.getString("token");//从重定向地址获取的token

        String userId = RedisHelper.get(REDIRECT_LOGIN_CACHE + token);
        AssertHelper.EX_BUSINESS.isNotBlank(userId, "redirectLogin.invalid_token");

        UcenterUser ucenterUser = ucenterUserMapper.selectByPrimaryKey(userId);
        Model ssoUserModel = ucenterNetApi.loadSsoUser(ucenterUser.getPersonId());
        AssertHelper.EX_BUSINESS.isNotEmpty(ssoUserModel, "login.person_info_load_failed");

        SsoUser ssoUser = (SsoUser) ssoUserModel.convertTo(SsoUser.class);

        String sgk = RandomHelper.uuid();
        ssoUser.setTicket(sgk);

        // 保存ticket 与用户的关系映射
        RedisHelper.set(TICKET_PREFIX + OS_PC + sgk, JsonHelper.obj2Json(ssoUser), LOGIN_EXPIRATION_TIME);
        // 保存用户登录的ticket信息
        RedisHelper.set(ACCOUNT_PREFIX + OS_PC + ucenterUser.getAccount(), sgk, LOGIN_EXPIRATION_TIME);

        // 更新登录状态
        this.doLogin(ucenterUser, true);

        // 记录登录日志
        this.loginLogSave(ssoUser, OS_PC, ucenterUser.getAccount(), 1);

        //删除改缓存
        RedisHelper.del(REDIRECT_LOGIN_CACHE + token);

        return ssoUser;
    }


    @Override
    public SsoUser doCheck(Model input) {
        return localLoginCheck.loginCheck(input);
    }
}
