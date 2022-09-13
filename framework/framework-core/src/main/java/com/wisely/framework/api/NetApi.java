package com.wisely.framework.api;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.AssertHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NetApi {

    protected NetApi() {

    }

    public NetApi(NetTools netTools) {
        AssertHelper.EX_SYSTEM.isNotEmpty(netTools,
                "Call {0}.initToole({1}) to initialize NET_TOOLS please",
                NetApi.class, NetTools.class);
        this.netTools = netTools;
        log.info("{}.NET_TOOLS has been init completed...", this.getName());
    }


    protected NetTools netTools;

    protected abstract String getName();


    /**
     * 请求方式封装
     *
     * @param url
     * @param request
     * @return
     */
    public Model doRequest(String url, Model request) {
        return doRequest(url, request, new TypeReference<Model>() {});
    }


    /**
     * 请求方式封装
     *
     * @param url
     * @param request
     * @param <T>
     * @return
     */
    public <T> T doRequest(String url, Model request, TypeReference<T> typeReference) {
        return netTools.doRequest(netTools.buildUrl(url), request, typeReference);
    }


    /**
     * 文件下载
     *
     * @param url
     * @param request
     * @param localAbsolutePath
     */
    public void download(String url, String httpMethod, Model request, String localAbsolutePath) {
        netTools.download(netTools.buildUrl(url), httpMethod, request, localAbsolutePath);
    }
}
