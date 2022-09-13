package com.wisely.framework.handler.annotation;

import java.lang.annotation.*;


/**
 * 请求处理转换器
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Converter {


    /**
     * 报文定义路径
     * @return
     */
    String path();

    /**
     * 开启请求数据转换
     * @return
     */
    boolean request() default true;

    /**
     * 开启响应数据转换
     * @return
     */
    boolean response() default true;

}
