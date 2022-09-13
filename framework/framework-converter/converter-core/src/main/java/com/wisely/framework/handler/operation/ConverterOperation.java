package com.wisely.framework.handler.operation;

import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;

public interface ConverterOperation<T> {


    Object validation(ConverterItemEntity itemEntity, Object data);

    Object converter(ConverterItemEntity itemEntity, Object data);

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

}
