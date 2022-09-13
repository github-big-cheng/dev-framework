package com.wisely.framework.cloud.handler.sentinel;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.eum.ExceptionCodeEnum;
import com.wisely.framework.helper.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class SentinelRestTemplateHandler {

    public final static String SENTINEL_HANDLER_METHOD = "handleException";
    public final static String SENTINEL_FALLBACK_METHOD = "fallback";

    final static Model RESULT = Model.builder()
            .set(ResponseBuilder.KEY_CODE, ExceptionCodeEnum.THIRD_PARTY.getCode())
            .set(ResponseBuilder.KEY_MESSAGE, "common.circuit_breaker_happened");


    /**
     * 服务流量控制处理
     *
     * @param request
     * @param body
     * @param execution
     * @param e
     * @return
     */
    public static ClientHttpResponse handleException(HttpRequest request,
                                                     byte[] body,
                                                     ClientHttpRequestExecution execution,
                                                     BlockException e) {
        log.error("SentinelRestTemplateHandler.handleException error:{}", e);
        return new SentinelClientHttpResponse(RESULT.toString());
    }


    /**
     * 服务熔断降级处理
     *
     * @param request
     * @param body
     * @param execution
     * @param e
     * @return
     */
    public static ClientHttpResponse fallback(HttpRequest request,
                                              byte[] body,
                                              ClientHttpRequestExecution execution,
                                              BlockException e) {
        log.error("SentinelRestTemplateHandler.fallback error:{}", e);
        return new SentinelClientHttpResponse(RESULT.toString());
    }
}
