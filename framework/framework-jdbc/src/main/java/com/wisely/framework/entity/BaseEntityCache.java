package com.wisely.framework.entity;

import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
public abstract class BaseEntityCache<T> extends AbstractCache<T> {

    /**
     * 获取存储key
     *
     * @return
     */
    protected abstract String getKey();

    /**
     * 获取存储的key
     *
     * @param t
     * @return
     */
    protected abstract byte[] loadField(T t);

    /**
     * 设置键值对
     *
     * @param model
     * @param t
     */
    protected abstract void setItem(Model<byte[], byte[]> model, T t);

    /**
     * 源数据
     *
     * @return
     */
    protected abstract List<T> loadData();

    /**
     * 初始化缓存
     */
    @Override
    public void initCache() {

        List<T> dataList = this.loadData();
        if (ValidHelper.isEmpty(dataList)) {
            log.warn("[{}] no cache data found!!!", this.getName());
            return;
        }

        Model<byte[], byte[]> model = Model.builder();
        // 以hash结构存储数据 account 对应 人员
        dataList.forEach(item -> this.setItem(model, item));

        if (model.isEmpty()) {
            return;
        }
        RedisHelper.hsetBytes(getKey(), model);
    }

    @Override
    public void clearCache() {
        RedisHelper.del(this.getKey());
    }

    @Override
    public void syncCache(T... objs) {
        if (ValidHelper.isEmpty(objs)) {
            return;
        }

        Model<byte[], byte[]> model = Model.builder();
        Stream.of(objs)
                .forEach(item -> this.setItem(model, item));
        RedisHelper.hsetBytes(getKey(), model);
    }

    @Override
    public void invalidate(T... objs) {
        if (ValidHelper.isEmpty(objs)) {
            return;
        }

        byte[][] fields = new byte[objs.length][];
        Stream.iterate(0, i -> i + 1).limit(fields.length)
                .forEach(inx -> fields[inx] = loadField(objs[inx]));

        RedisHelper.hdel(getKey().getBytes(), fields);
    }
}
