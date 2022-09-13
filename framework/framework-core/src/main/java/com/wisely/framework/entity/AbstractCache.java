package com.wisely.framework.entity;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
public abstract class AbstractCache<T> {


    @PostConstruct
    protected void initialize() {
        log.info("Cache [{}] has been created...", this.getName());
    }

    /**
     * 缓存名称
     *
     * @return
     */
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * 加载缓存方法
     */
    public abstract void initCache();

    /**
     * 清理缓存方法
     */
    public abstract void clearCache();

    /**
     * 更新
     *
     * @param objs
     */
    public abstract void syncCache(T... objs);

    /**
     * 删除指定缓存
     *
     * @param objs
     */
    public abstract void invalidate(T... objs);
}
