package com.wisely.framework.controller;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.parser.ConverterDefineParser;
import com.wisely.framework.helper.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/converter")
@Slf4j
public class ConverterController {

    @Resource
    ConverterDefineParser converterDefineParser;

    @RequestMapping("/list")
    public Object list() {

        List<ConverterEntity> list = Lists.newArrayList();

        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringHelper.getBean(RequestMappingHandlerMapping.class);
        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            Converter converter = handlerMethod.getMethodAnnotation(Converter.class);
            if (ValidHelper.isNull(converter)) {
                return;
            }

            ConverterEntity entity = converterDefineParser.loadConfig(converter.path());
            if (ValidHelper.isEmpty(entity) ||
                    // 2.6 开始调整了路径匹配策略
                    ValidHelper.isEmpty(requestMappingInfo.getPathPatternsCondition())) {
                return;
            }
            entity.setExtendField("url", StringHelper.join(requestMappingInfo.getPathPatternsCondition().getPatterns(), ","));
            list.add(entity);
        });

        return ResponseBuilder.buildSuccess(list);
    }


    /**
     * 刷新指定报文定义缓存
     *
     * @return
     */
    @RequestMapping("/refresh")
    public Object refresh() throws Exception {
        Model input = RequestHelper.getInput();
        converterDefineParser.refreshConfig(input.getString("defineKey"));
        return ResponseBuilder.buildSuccess();
    }


    @RequestMapping("/load")
    public Object load(String defineKey) {
        AssertHelper.EX_VALIDATION.isNotBlank(defineKey, "common.parameter_required.defineKey");
        return ResponseBuilder.buildSuccess(converterDefineParser.loadConfig(defineKey));
    }
}
