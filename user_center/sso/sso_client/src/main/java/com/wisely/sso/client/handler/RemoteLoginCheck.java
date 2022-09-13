package com.wisely.sso.client.handler;

import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BaseException;
import com.wisely.framework.exception.BusinessException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.filter.LoginFilterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.wisely.framework.helper.ResponseBuilder.*;

@Slf4j
public class RemoteLoginCheck implements LoginCheck {

    @Autowired
    private LoginFilterProperties filterProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SsoUser loginCheck(Model input) throws BusinessException {

        AssertHelper.EX_SYSTEM.isNotEmpty(filterProperties, "loginFilter.filterProperties_not_config");
        AssertHelper.EX_SYSTEM.isNotBlank(filterProperties.getCheckLoginUrl(), "loginFilter.filterProperties_checkLoginUrl_not_config");

        ResponseEntity<Model> resultResponse = restTemplate.postForEntity(filterProperties.getCheckLoginUrl(), input, Model.class);
        Model result = resultResponse.getBody();
        log.debug("result = 【{}】", result.toString());
        int code = result.getInt(KEY_CODE, -1);
        if (code != 0) {
            throw BaseException.builder(code, result.getString(KEY_MESSAGE));
        }

        return JsonHelper.cast(result.get(KEY_DATA), SsoUser.class);
    }
}
