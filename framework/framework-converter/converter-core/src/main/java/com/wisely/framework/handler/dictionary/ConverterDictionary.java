package com.wisely.framework.handler.dictionary;

import com.wisely.framework.handler.entity.ConverterItemEntity;

public interface ConverterDictionary<T> {

    /**
     * 是否接收当前类型的值转换
     *
     * @param itemEntity
     * @return
     */
    boolean accept(ConverterItemEntity itemEntity);


    /**
     * 获取对应的值
     *
     * @param itemEntity
     * @param value
     * @return
     */
    T loadValue(ConverterItemEntity itemEntity, Object value);
}
