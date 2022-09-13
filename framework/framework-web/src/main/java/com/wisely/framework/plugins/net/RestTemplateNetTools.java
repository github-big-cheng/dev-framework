package com.wisely.framework.plugins.net;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.wisely.framework.api.NetTools;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RestTemplateNetTools implements NetTools {

    final static String PREFIX_HTTPS = "https://";


    public RestTemplateNetTools(String requestUrl, RestTemplate restTemplate) {
        this.requestUrl = requestUrl;
        this.restTemplate = restTemplate;
    }

    private String requestUrl;

    private RestTemplate restTemplate;

    /**
     * 封装请求路径
     *
     * @param interfaceName
     * @return
     */
    public String buildUrl(String interfaceName) {
        // 服务地址，后期考虑redis取数
        return requestUrl + interfaceName;
    }


    /**
     * 请求方式封装
     *
     * @param url
     * @param request
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T doRequest(String url, Model request, TypeReference<T> typeReference) {
        AssertHelper.EX_VALIDATION.isNotBlank(url, "common.parameter_required.url");
        AssertHelper.EX_VALIDATION.isNotEmpty(typeReference, "common.parameter_required.clazz");

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        if (ValidHelper.isNotEmpty(request)) {
            for (Object key : request.keySet()) {
                paramMap.put(String.valueOf(key), Lists.newArrayList(request.get(key)));
            }
        }

        String response = this.restTemplate.postForObject(url, paramMap, String.class);
        return ValidHelper.isNotEmpty(typeReference) &&
                !StringHelper.equals(typeReference.getType().getTypeName(), "java.lang.String") ?
                JsonHelper.json2Obj(response, typeReference) : (T) response;
    }


    /**
     * 静态资源下载
     *
     * @param url
     * @param method            请求方式
     * @param request
     * @param localAbsolutePath 含文件名的本地保存绝对路径
     */
    @Override
    public void download(String url, String method, Model request, String localAbsolutePath) {

        AssertHelper.EX_VALIDATION.isNotBlank(url, "common.parameter_required.url");
        AssertHelper.EX_VALIDATION.isNotBlank(localAbsolutePath, "common.parameter_required.localPath");

        // 请求参数
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        if (ValidHelper.isNotEmpty(request)) {
            for (Object key : request.keySet()) {
                paramMap.put(String.valueOf(key), Lists.newArrayList(request.get(key)));
            }
        }

        // 请求方式
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (ValidHelper.isEmpty(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }

        // 发送请求
        ResponseEntity<byte[]> responseEntity =
                restTemplate.exchange(url, httpMethod, new HttpEntity<>(paramMap), byte[].class);
        byte[] result = responseEntity.getBody();
        AssertHelper.EX_SYSTEM.isNotEmpty(result, "fileDownload.no_data_found");

        Path path = Paths.get(localAbsolutePath);
        File parentFile = path.toFile().getParentFile();
        try {
            if (!parentFile.exists()) {
                Files.createDirectories(parentFile.toPath());
            }

            Files.write(path, result, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw SystemException.builder(e, "file.file_create_error");
        }
    }
}
