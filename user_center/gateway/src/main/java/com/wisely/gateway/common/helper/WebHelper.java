package com.wisely.gateway.common.helper;

import com.google.common.collect.Lists;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Slf4j
public class WebHelper {

    // 未知IP
    private final static String UNKNOWN = "unknown";
    // 本地 IP
    private final static String LOCALHOST_IP4 = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST_IP6 = "127.0.0.1";

    private final static List<String> HEADERS =
            Lists.newArrayList(
                    "X-Forwarded-For",
                    "x-forwarded-for",
                    "Proxy-Client-IP",
                    "WL-Proxy-Client-IP",
                    "HTTP_CLIENT_IP",
                    "HTTP_X_FORWARDED_FOR",
                    "X-Real-IP"
            );

    public static String getServerHttpRequestIpAddress(ServerHttpRequest request) {

        String headerKey =
                HEADERS.stream()
                        .filter(header -> {
                            List<String> ips = request.getHeaders().get(header);
                            return ValidHelper.isNotEmpty(ips) && StringHelper.equalsIgnoreCase(ips.get(0), UNKNOWN);
                        })
                        .findFirst()
                        .orElse(null);

        if (StringHelper.isNotBlank(headerKey)) {
            return StringHelper.join(request.getHeaders().get(headerKey), ",");
        }

        //兼容k8s集群获取ip
        String ip = request.getRemoteAddress().getAddress().getHostAddress();
        if (LOCALHOST_IP4.equalsIgnoreCase(ip) || LOCALHOST_IP6.equalsIgnoreCase(ip)) {
            //根据网卡取本机配置的IP
            InetAddress iNet = null;
            try {
                iNet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                log.error("getClientIp error: {}", e);
            }
            ip = iNet.getHostAddress();
        }
        return ip;
    }
}
