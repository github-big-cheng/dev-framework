package com.wisely.framework.handler.data;


import com.wisely.framework.handler.entity.ConverterItemEntity;

public interface DataConverter<T> {

    /**
     * 通用accept方法
     *
     * @param item
     * @return
     */
    default boolean accept(ConverterItemEntity item) {
        return false;
    }

    /**
     * 是否接收请求转换
     *
     * @param item
     * @return
     */
    default boolean requestAccept(ConverterItemEntity item) {
        return this.accept(item);
    }

    /**
     * 是否接收响应效验
     *
     * @param item
     * @return
     */
    default boolean responseAccept(ConverterItemEntity item) {
        return this.accept(item);
    }


    /**
     * 数据校验
     *
     * @param item
     * @param o
     * @return
     */
    default Object validation(ConverterItemEntity item, Object o) {
        return o;
    }


    /**
     * 数据转换
     *
     * @param item
     * @param o
     * @return
     */
    default T convert(ConverterItemEntity item, Object o) {
        return (T) o;
    }

}
