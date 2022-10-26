package com.wisely.framework.handler.operation;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.def.DefaultJsonResponseConverterOperation;
import com.wisely.framework.handler.operation.def.DefaultRequestValidationOperation;

import java.util.List;


/**
 * 默认的消息转换器
 * <p>
 * response converter used with com.wisely.framework.entity.Model
 */
public class DefaultConverterOperation implements ConverterOperation<Model> {

    public DefaultConverterOperation(List<DataConverter> dataConverts) {
        this.requestValidationOperation = new DefaultRequestValidationOperation(dataConverts);
        this.responseConverterOperation = new DefaultJsonResponseConverterOperation(dataConverts);
    }

    private DefaultRequestValidationOperation requestValidationOperation;
    private DefaultJsonResponseConverterOperation responseConverterOperation;


    @Override
    public void request(ConverterEntity entity, Model request) {
        requestValidationOperation.request(entity, request);
    }

    /**
     * 响应处理
     *
     * @param entity
     * @param data
     * @return
     */
    @Override
    public Object response(ConverterEntity entity, Object data) {
        return responseConverterOperation.response(entity, data);
    }


    @Override
    public Object validation(ConverterItemEntity itemEntity, Object data) {
        return this.requestValidationOperation.validation(itemEntity, data);
    }

    @Override
    public Object converter(ConverterItemEntity itemEntity, Object data) {
        return this.responseConverterOperation.converter(itemEntity, data);
    }


}
