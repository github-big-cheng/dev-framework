package com.wisely.framework.handler.operation;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.def.DefaultJsonResponseConverterOperation;
import com.wisely.framework.handler.operation.def.DefaultRequestValidationOperation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 默认的消息转换器
 * <p>
 * response converter used with ResponseHelper.buildSuccess
 */
public class DefaultConverterOperation implements ConverterOperation<Model> {


    @Autowired
    public DefaultConverterOperation(List<DataConverter> dataConverts) {
        this.converterList.addAll(dataConverts);
        this.requestValidationOperation = new DefaultRequestValidationOperation(this);
        this.responseConverterOperation = new DefaultJsonResponseConverterOperation(this);
    }


    private final List<DataConverter> converterList = Lists.newArrayList();


    /**
     * use Pipeline instead of it
     *
     * @return
     */
    @Deprecated
    public List<DataConverter> getConverters() {
        return this.converterList;
    }


    @Override
    public Object validation(ConverterItemEntity itemEntity, Object data) {

        Object value = data;

        // 普通模式
        for (int i = 0; i < this.converterList.size(); i++) {
            DataConverter converter = this.converterList.get(i);
            if (converter.requestAccept(itemEntity)) {
                value = converter.validation(itemEntity, value);
            }
        }
        return value;
    }

    @Override
    public Object converter(ConverterItemEntity itemEntity, Object data) {

        Object value = data;

        // 普通模式
        for (int i = 0; i < this.converterList.size(); i++) {
            DataConverter converter = this.converterList.get(i);
            if (converter.responseAccept(itemEntity)) {
                value = converter.convert(itemEntity, value);
            }
        }
        return value;
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

}
