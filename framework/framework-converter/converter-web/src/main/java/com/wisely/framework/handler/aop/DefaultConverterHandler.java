package com.wisely.framework.handler.aop;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.annotation.Converter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.operation.ConverterOperation;
import com.wisely.framework.handler.parser.ConverterDefineParser;
import com.wisely.framework.helper.RequestHelper;
import com.wisely.framework.helper.ResponseBuilder;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;

@Aspect
@Slf4j
public class DefaultConverterHandler implements ConverterHandler {

    @Resource
    ConverterDefineParser converterDefineParser;

    @Resource
    ConverterOperation converterOperation;


    /**
     * around
     * 基于标准格式
     * {
     * code: 0,
     * message: "成功",
     * data: null
     * }
     *
     * @param joinPoint
     * @param converter
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(converter)")
    public Object around(ProceedingJoinPoint joinPoint, Converter converter) throws Throwable {

        ConverterEntity entity = converterDefineParser.loadConfig(converter.path());
        if (converter.request() && ValidHelper.isNotEmpty(entity)) {
            // can do some validation here
            converterOperation.request(entity, RequestHelper.getInput());
        }

        Object result = joinPoint.proceed();
        if (converter.response() && ValidHelper.isNotEmpty(entity)) {
            // do response convert or filter here
            Model response = Model.parseObject(result);
            response.set(ResponseBuilder.KEY_DATA,
                    converterOperation.response(entity, response.get(ResponseBuilder.KEY_DATA)));
            result = response;
        }

        return result;
    }

}
