package com.wisely.framework.handler.operation;

import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;

public interface ConverterOperation<T> {

    /**
     * 请求处理
     *
     * @param entity
     * @param request
     */
    void request(ConverterEntity entity, T request);


    /**
     * 响应处理
     *
     * @param entity
     * @param response
     * @return
     */
    Object response(ConverterEntity entity, Object response);

    /**
     * 数据项校验
     *
     * @param itemEntity
     * @param data
     * @return
     */
    Object validation(ConverterItemEntity itemEntity, Object data);

    /**
     * 数据项转换
     *
     * @param itemEntity
     * @param data
     * @return
     */
    Object converter(ConverterItemEntity itemEntity, Object data);

}
