package com.wisely.framework.helper;

import com.wisely.framework.entity.FrameworkRequestWrapper;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.message.MessageConvert;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * RequestHelper
 */
@Slf4j
public class RequestHelper {


    private RequestHelper() {
    }


    private final static ThreadLocal<FrameworkRequestWrapper> THREAD_LOCAL_MAP = new ThreadLocal<>();

    public static void setRequest(FrameworkRequestWrapper request) {
        THREAD_LOCAL_MAP.set(request);
    }

    /**
     * 清理
     */
    public static void clear() {
        THREAD_LOCAL_MAP.remove();
    }

    /**
     * 获取当前请求的 Request对象
     *
     * @return Request对象
     */
    @Nullable
    public static HttpServletRequest getRequest() {

        FrameworkRequestWrapper requestWrapper = THREAD_LOCAL_MAP.get();
        if (ValidHelper.isNotEmpty(requestWrapper)) {
            return requestWrapper;
        }

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra == null ? null : sra.getRequest();
    }

    /**
     * 获取当前请求的 Request对象
     *
     * @return Response对象
     */
    @Nullable
    public static HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra == null ? null : sra.getResponse();
    }


    /**
     * 获取服务完整根路径
     *
     * @return
     * @Deprecated Don't user!!! Use RedisHelper.get to instead of it
     */
    @Deprecated
    @Nullable
    public static String getFullServerPath() {
        HttpServletRequest request = getRequest();
        return request == null ? null :
                request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
    }


    /**
     * 获取当前请求的 ContextPath
     *
     * @return
     * @Deprecated Don't user!!! Use RedisHelper.get to instead of it
     */
    @Deprecated
    @Nullable
    public static String getContextPath() {
        return getRequest() == null ? null : getRequest().getContextPath();
    }

    /**
     * 获取session
     *
     * @return
     */
    @Nullable
    public static HttpSession getSession() {
        return getRequest() == null ? null : getRequest().getSession();
    }

    /**
     * 获取sessionId
     *
     * @return
     */
    @Nullable
    public static String getSessionId() {
        return getSession() == null ? null : getSession().getId();
    }

    /**
     * 获取cookies
     *
     * @return
     */
    @Nullable
    public static Cookie[] getCookies() {
        return getRequest() == null ? null : getRequest().getCookies();
    }


    /**
     * 获取cookie中指定值
     *
     * @param key
     * @return
     */
    public static String getCookie(String key) {
        return getCookie(key, null);
    }

    /**
     * 获取cookie中指定值
     *
     * @param key
     * @return
     */
    public static String getCookie(String key, String defaultVal) {
        Cookie[] cookies = getCookies();
        if (ValidHelper.isEmpty(cookies)) {
            return defaultVal;
        }

        for (Cookie cookie : cookies) {
            if (StringHelper.equals(cookie.getName(), key)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    return defaultVal;
                }
            }
        }

        return defaultVal;
    }

    /**
     * 获取发起当前请求的客户端IP
     *
     * @return 客户端IP地址
     */
    public static String getIP() {
        return getIP(getRequest());
    }

    /**
     * 获取IP地址
     * 多次反向代理后会有多个ip值，第一个ip才是真实ip
     *
     * @param request 请求
     * @return request发起客户端的IP地址
     */
    public static String getIP(HttpServletRequest request) {
        if (ValidHelper.isNull(request)) {
            return "0.0.0.0";
        }

        final String UNKNOWN_IP = "unknown";
        final List<String> headKeys =
                Lists.newArrayList(
                        "X-Forwarded-For",
                        "X-Real-IP",
                        "Proxy-Client-IP",
                        "WL-Proxy-Client-IP",
                        "HTTP_CLIENT_IP",
                        "HTTP_X_FORWARDED_FOR"
                );

        String ip =
                headKeys.stream().filter(key -> {
                    String val = request.getHeader(key);
                    return StringHelper.isNotBlank(val) && !StringHelper.equalsIgnoreCase(val, UNKNOWN_IP);
                }).findFirst().orElse(null);
        return StringHelper.isNotBlank(ip) ? request.getHeader(ip) : request.getRemoteAddr();
    }

    /**
     * 封装 转换Request 为 Model DATA
     *
     * @return
     */
    public static Model getInput(HttpServletRequest request) {
        return getInput(request, false);
    }

    public static Model getInput() {
        return getInput(getRequest(), false);
    }

    /**
     * 封装转换Request 为 Model DATA
     *
     * @param request
     * @return
     */
    public static Model getInput(HttpServletRequest request, boolean clearHistory) {

        Model input = (Model) request.getAttribute("input");
        if (null != input && !input.isEmpty() && !clearHistory) {
            return input;
        } else {
            input = Model.builder("REQUEST_DATA");
        }
        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            String[] values = request.getParameterValues(key);
            for (String value : values) {
                if (input.containsKey(key) && !key.equalsIgnoreCase("id")) {
                    Object temp = input.get(key);
                    if (temp instanceof List) {
                        ((List) temp).add(value);
                    } else {
                        List temps = new ArrayList();
                        temps.add(temp);
                        temps.add(value);
                        input.put(key, temps);
                    }
                } else {
                    input.put(key, value);
                }
            }
        }

        // payload方式参数
        if (StringHelper.contains(request.getContentType(), "application/json")) {
            try (BufferedReader br = request.getReader()) {
                StringBuffer sb = new StringBuffer();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                String lines = sb.toString();
                if (StringHelper.isNotBlank(lines)) {
                    input.putAll(JsonHelper.json2Obj(lines, Map.class));
                }
            } catch (Exception e) {
                log.warn("request.getInputStream error:{}", e);
            }
        }

        // 文件方式的请求
        MultipartResolver multipartResolver = SpringHelper.getBean(MultipartResolver.class);
        if (multipartResolver != null
                && multipartResolver.isMultipart(request)) {
            MultipartRequest multipartRequest = multipartResolver.resolveMultipart(request);
            for (String fileKey : multipartRequest.getMultiFileMap().keySet()) {
                input.set(fileKey, multipartRequest.getMultiFileMap().get(fileKey));
            }
        }

        request.setAttribute("input", input);
        return input;
    }


    /**
     * 前台消息返回
     *
     * @param code
     * @param message
     * @param data
     */
    public static void writeResponse(int code, String message, Object data) {
        PrintWriter out = null;
        try {
            if (SpringHelper.hasBean(MessageConvert.class)) {
                message = SpringHelper.getBean(MessageConvert.class).messageConvert(message);
            }

            HttpServletResponse response = RequestHelper.getResponse();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JsonHelper.obj2Json(ResponseBuilder.build(code, message, data)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}
