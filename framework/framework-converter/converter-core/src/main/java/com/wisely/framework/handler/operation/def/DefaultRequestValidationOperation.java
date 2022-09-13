package com.wisely.framework.handler.operation.def;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.DefaultConverterOperation;
import com.wisely.framework.helper.ValidHelper;

import java.util.List;


public class DefaultRequestValidationOperation implements ConverterConstants {

    public DefaultRequestValidationOperation(DefaultConverterOperation converterOperation) {
        this.converterOperation = converterOperation;
    }

    private DefaultConverterOperation converterOperation;

    public void request(ConverterEntity entity, Model request) {

        if (ValidHelper.isEmpty(entity)) {
            return;
        }

        // 执行校验
        this.validRequest(entity.getSend(), request);
    }

    protected void validRequest(List<ConverterItemEntity> itemEntityList, Model request) {

        if (ValidHelper.isEmpty(itemEntityList)) {
            return;
        }
        if (ValidHelper.isNull(request)) {
            request = Model.builder();
        }

        Model finalRequest = request;
        itemEntityList.forEach(item -> {
            Object data = converterOperation.validation(item, finalRequest.get(item.getKey()));
            finalRequest.set(item.getName(), data);
        });
    }

}
