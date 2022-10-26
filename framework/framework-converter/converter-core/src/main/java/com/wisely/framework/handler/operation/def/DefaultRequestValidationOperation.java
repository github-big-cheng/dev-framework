package com.wisely.framework.handler.operation.def;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.ValidHelper;

import java.util.List;


public class DefaultRequestValidationOperation implements ConverterConstants {

    public DefaultRequestValidationOperation(List<DataConverter> converterList) {
        this.converterList = converterList;
    }

    /**
     * 处理处理器集合
     *
     * @return
     */
    private List<DataConverter> converterList;


    public Object validation(ConverterItemEntity itemEntity, Object value) {

        for (int i = 0; i < this.converterList.size(); i++) {
            DataConverter converter = this.converterList.get(i);
            if (converter.requestAccept(itemEntity)) {
                value = converter.validation(itemEntity, value);
            }
        }
        return value;
    }


    public void request(ConverterEntity entity, Model request) {

        if (ValidHelper.isEmpty(entity)) {
            return;
        }

        // 执行校验
        if (ValidHelper.isEmpty(entity.getSend())) {
            return;
        }
        if (ValidHelper.isNull(request)) {
            request = Model.builder();
        }

        Model finalRequest = request;
        entity.getSend().forEach(item -> {
            Object data = this.validation(item, finalRequest.get(item.getKey()));
            finalRequest.set(item.getName(), data);
        });
    }

}
