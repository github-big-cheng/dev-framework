package com.wisely.framework.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Nonnull;

/**
 * Spring工具类
 */
@Slf4j
public class SpringHelper implements ApplicationContextAware {


    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext)
            throws BeansException {
        initContext(applicationContext);
        log.info("Spring Context自动初始化完成，可以通过SpringHelper类提供的方法来获取Bean.");
    }


    private SpringHelper() {
    }

    private static ApplicationContext context = null;

    /**
     * 初始化Spring上下文
     *
     * @param ctx 上下文对象
     */
    private static void initContext(ApplicationContext ctx) {
        if (ctx == null) {
            log.warn("ApplicationContext is null.");
            return;
        }
        context = ctx;
    }


    public static ApplicationContext getInstance() {
        AssertHelper.EX_SYSTEM.isNotEmpty(context, "system.applicationContext_init_failed");
        return context;
    }


    /**
     * 检查是否有指定bean
     *
     * @param name
     * @return
     */
    public static boolean hasBean(String name) {
        return context == null ? false : context.containsBean(name);
    }


    /**
     * 检查是否有指定bean
     *
     * @param cls
     * @return
     */
    public static boolean hasBean(Class cls) {
        return context == null ? false : ValidHelper.isNotEmpty(context.getBeansOfType(cls));
    }

    /**
     * 根据类型获取Bean
     *
     * @param cls Bean类
     * @param <T> Bean类型
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> cls) {
        return context == null ? null : context.getBean(cls);
    }

    /**
     * 根据名称获取Bean
     *
     * @param name Bean名称
     * @return Bean对象
     */
    public static Object getBean(String name) {
        return context == null ? null : context.getBean(name);
    }

    /**
     * 根据Bean名称和类获取Bean对象
     *
     * @param name Bean名称
     * @param cls  Bean类
     * @param <T>  Bean类型
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> cls) {
        return context == null ? null : context.getBean(name, cls);
    }

}
