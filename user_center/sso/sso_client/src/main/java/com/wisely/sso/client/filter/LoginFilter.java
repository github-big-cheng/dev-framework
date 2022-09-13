package com.wisely.sso.client.filter;

import com.wisely.framework.entity.FrameworkRequestWrapper;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BusinessException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.sso.client.handler.LoginCheck;
import com.wisely.sso.client.handler.LoginHandler;
import com.wisely.sso.client.helper.UserHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义登录拦截器
 */
@Slf4j
public class LoginFilter implements Filter, SsoConstants {

    private final static PathMatcher ant = new AntPathMatcher();

    /**
     * 免登录校验
     */
    private Set<String> excludedPages;
    /**
     * 免权限校验
     */
    private Set<String> excludedAuth;


    @Resource
    LoginFilterProperties loginFilterProperties;
    @Resource
    private LoginCheck loginCheck;


    @Override
    public void init(FilterConfig filterConfig) {

        Model initParameters = Model.builder(loginFilterProperties.getInitParameters());

        // 登录校验白名单
        List<String> ignoreLoginList = initParameters.getSpitList("excludes", ",");
        excludedPages = ignoreLoginList.stream().collect(Collectors.toSet());
        log.debug("excludedPages:{}", excludedPages);

        // 权限校验白名单
        List<String> ignoreAuthList = initParameters.getSpitList("excludedAuth", ",");
        excludedAuth = ignoreAuthList.stream().collect(Collectors.toSet());
        log.debug("excludedAuth:{}", excludedAuth);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FrameworkRequestWrapper wrapper = (FrameworkRequestWrapper) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Model input = RequestHelper.getInput();
        String osType = input.getString(OS_KEY, OS_PC);
        String ticket = input.getString(SSO_KEY);

        // 消息体内获取ticket，方便调试，可考虑移除
        if (StringHelper.isEmpty(ticket)) {
            ticket = wrapper.getHeader(SSO_KEY);
            if (StringHelper.isNotBlank(ticket)) {
                input.set(SSO_KEY, ticket);
            }
        }

        // 安卓，客户端签名验证
        if (StringHelper.equalsIgnoreCase(osType, OS_ANDROID)) {
            // 获取data参数，并赋值到request
            if (!LoginHandler.sign(input, wrapper, loginFilterProperties.getExpiredTime())) {
                return;
            }
        }

        // 登录校验
        if (needLoginCheck(wrapper)) {
            boolean flag = false;
            try {
                Model params = Model.builder();
                params.set(OS_KEY, osType)
                        .set(SSO_KEY, ticket);

                String action = StringHelper.removeStart(wrapper.getServletPath(), "/");
                if (needAuthCheck(action)) {
                    params.set("action", action);
                }

                // 登录及权限校验
                SsoUser ssoUser = loginCheck.loginCheck(params);
                AssertHelper.EX_VALIDATION.isNotEmpty(ssoUser, "login.login_checked_failed");

                // 设置线程用户信息线程变量
                flag = true;
                UserHelper.setUser(ssoUser);

            } catch (BusinessException e) {
                // response loginCheck message to client
                RequestHelper.writeResponse(e.getCode(), e.getMessage(), loginFilterProperties.getRedirectUrl());
            } finally {
                if (flag) {
                    UserHelper.clear();
                }
            }
        }

        try {
            filterChain.doFilter(wrapper, response);
        } finally {
            // clear ThreadLocal
            UserHelper.clear();
        }
    }

    /**
     * 判断白名单
     *
     * @param request
     * @return
     */
    private boolean needLoginCheck(FrameworkRequestWrapper request) {
        final String url = request.getServletPath();
        if (ValidHelper.isNotEmpty(excludedPages)) {
            return !excludedPages.stream().anyMatch(pattern -> {
                log.debug("needLoginCheck.url:{}, pattern:{}", url, pattern);
                return ant.match(pattern, url);
            });
        }
        return true;
    }


    /**
     * 是否需要权限校验
     *
     * @param action
     * @return
     */
    private boolean needAuthCheck(String action) {
        if (ValidHelper.isNotEmpty(excludedAuth)) {
            return !excludedAuth.stream().anyMatch(pattern -> {
                log.debug("needAuthCheck.url:{}, pattern:{}", action, pattern);
                return ant.match(pattern, action);
            });
        }
        return true;
    }

    @Override
    public void destroy() {
        log.info("login filter destroy...");
    }
}
