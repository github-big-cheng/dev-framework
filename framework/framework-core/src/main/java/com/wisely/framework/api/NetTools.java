package com.wisely.framework.api;

import com.wisely.framework.entity.Model;
import com.fasterxml.jackson.core.type.TypeReference;

public interface NetTools {


    /**
     * 构建请求Url
     *      contextPath可以从配置文件、redis等地方读取拼装
     * @param interfaceName
     * @return
     */
    String buildUrl(String interfaceName);

    /**
     * 请求
     *
     * @param url
     * @param request
     * @param typeReference
     * @param <T>
     * @return
     */
    <T> T doRequest(String url, Model request, TypeReference<T> typeReference);


    /**
     * 静态资源下载
     *
     * @param url
     * @param httpMethod
     * @param request
     * @param localPath
     */
    default void download(String url, String httpMethod, Model request, String localPath) {
        // do nothing
    }
}
