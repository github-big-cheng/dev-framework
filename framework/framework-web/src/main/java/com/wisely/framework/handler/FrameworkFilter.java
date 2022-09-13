package com.wisely.framework.handler;

import com.wisely.framework.entity.FrameworkRequestWrapper;
import com.wisely.framework.helper.RequestHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用于request的包装，支持流的重复读取
 */
@Slf4j
public class FrameworkFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        FrameworkRequestWrapper wrapper = new FrameworkRequestWrapper(request);
        RequestHelper.setRequest(wrapper);
        try {
            filterChain.doFilter(wrapper, response);
        } finally {
            // 清理线程变量
            RequestHelper.clear();
        }
    }
}
