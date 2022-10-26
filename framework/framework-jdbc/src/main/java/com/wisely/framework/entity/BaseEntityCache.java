package com.wisely.framework.entity;

import com.google.common.collect.Lists;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Slf4j
public abstract class BaseEntityCache<T> extends AbstractCache<T> {

    /**
     * 获取缓存处理对象
     *
     * @return
     */
    protected abstract List<CacheLoader<T>> cacheLoaders();

    /**
     * 获取数据
     *
     * @return
     */
    protected abstract List<T> loadData();

    /**
     * 设置缓存
     */
    protected void setCaches(List<T> dataList, Model<String, Model<byte[], byte[]>> cacheModel) {

        if (ValidHelper.isEmpty(dataList)) {
            return;
        }

        List<CacheLoader<T>> cacheLoaders = this.cacheLoaders();
        if (ValidHelper.isEmpty(cacheLoaders)) {
            return;
        }

        dataList.forEach(item ->
                cacheLoaders().forEach(loader ->
                        loader.addConsumer().accept(cacheModel.getModel(loader.key(), true), item)));
    }

    /**
     * 从CacheModel保存到Redis
     *
     * @param cacheModel
     */
    protected void toRedis(Model<String, Model<byte[], byte[]>> cacheModel) {

        if (cacheModel.isEmpty()) {
            return;
        }

        // 以pipeline方式批处理数据
        cacheModel.forEach((key, model) -> {
            if (ValidHelper.isEmpty(model)) {
                return;
            }

            RedisHelper.hsetBytes(key, model);
        });
    }

    /**
     * 初始化缓存
     */
    @Override
    public void initCache() {

        if (ValidHelper.isEmpty(this.cacheLoaders())) {
            log.warn("[{}] cache-loaders not define!!!", this.getName());
            return;
        }

        List<T> dataList = this.loadData();
        if (ValidHelper.isEmpty(dataList)) {
            log.warn("[{}] no cache data found!!!", this.getName());
            return;
        }

        Model<String, Model<byte[], byte[]>> cacheModel = Model.builder();
        this.setCaches(dataList, cacheModel);

        // 保存数据到Redis
        this.toRedis(cacheModel);
    }

    /**
     * 清除缓存
     */
    @Override
    public void clearCache() {
        this.cacheLoaders().forEach(loader -> RedisHelper.del(loader.key()));
    }

    /**
     * 同步指定数据
     *
     * @param objs
     */
    @Override
    public void syncCache(T... objs) {
        if (ValidHelper.isEmpty(objs)) {
            return;
        }

        // 设置缓存集合
        Model<String, Model<byte[], byte[]>> cacheModel = Model.builder();
        this.setCaches(Lists.newArrayList(objs), cacheModel);

        // 保存数据到Redis
        this.toRedis(cacheModel);
    }

    @Override
    public void invalidate(T... objs) {
        if (ValidHelper.isEmpty(objs)) {
            return;
        }

        List<CacheLoader<T>> cacheLoaders = this.cacheLoaders();
        if (ValidHelper.isEmpty(cacheLoaders)) {
            return;
        }

        Model<String, List<byte[]>> cacheModel = Model.builder();
        Stream.of(objs).forEach(obj ->
                cacheLoaders.forEach(loader ->
                        loader.delConsumer().accept(cacheModel.getList(loader.key(), true), obj)));

        if (ValidHelper.isEmpty(cacheModel)) {
            return;
        }

        cacheModel.forEach((key, list) -> {
            if (ValidHelper.isEmpty(list)) {
                return;
            }

            RedisHelper.hdel(key.getBytes(), list.toArray(new byte[0][]));
        });
    }


    protected static abstract class CacheLoader<T> {
        /**
         * 缓存keys
         *
         * @return
         */
        public abstract String key();

        /**
         * 获取执行生成缓存的函数数组
         *
         * @return
         */
        public BiConsumer<Model<byte[], byte[]>, T> addConsumer() {
            return (cacheModel, t) -> { };
        }

        /**
         * 获取执行删除缓存的函数数组
         *
         * @return
         */
        public BiConsumer<List<byte[]>, T> delConsumer() {
            return (list, t) -> { };
        }
    }
}
