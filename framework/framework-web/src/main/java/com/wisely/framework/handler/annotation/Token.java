package com.wisely.framework.handler.annotation;

import java.lang.annotation.*;

/**
 * 表单防重复提交注解
 * 需与TokenPlugins配合使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {

}
