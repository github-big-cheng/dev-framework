package com.wisely.framework.handler.operation.def;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;

import java.util.List;


/**
 * 返回json格式数据
 */
public class DefaultJsonResponseConverterOperation implements ConverterConstants {

    public DefaultJsonResponseConverterOperation(List<DataConverter> converterList) {
        this.converterList = converterList;
    }

    /**
     * 处理处理器集合
     *
     * @return
     */
    private List<DataConverter> converterList;


    public Object response(ConverterEntity entity, Object response) {
        if (ValidHelper.isEmpty(entity) || ValidHelper.isEmpty(entity.getRcv())) {
            return null;
        }
        return buildResponse(entity.getRcv(), response);
    }


    protected Object buildResponse(List<ConverterItemEntity> rcvList, Object response) {

        Model result = Model.builder();

        if (rcvList.size() == 1) {
            ConverterItemEntity itemEntity = rcvList.get(0);
            // 处理直接直接返回的情况
            if (DataHelper.getBoolean(itemEntity.getExtendField(ConverterConstants.ATTR_IGNORE_KEY))) {
                String key = StringHelper.isNotBlank(itemEntity.getTarget()) ? itemEntity.getTarget() : itemEntity.getName();
                response = Model.builder().set(key, response);

                final Model data = Model.parseObject(response);
                rcvList.forEach(item -> this.buildResponseItem(item, data, result));

                return result.isEmpty() ? result : result.get(result.keySet().iterator().next());
            }
        }

        final Model data = Model.parseObject(response);
        rcvList.forEach(item -> this.buildResponseItem(item, data, result));
        return result;
    }


    protected void buildResponseItem(ConverterItemEntity item, Model response, Model data) {
        Object value = this.converter(item, response.get(item.getKey()));
        data.set(item.getName(), value);
    }


    public Object converter(ConverterItemEntity itemEntity, Object value) {
        for (int i = 0; i < this.converterList.size(); i++) {
            DataConverter converter = this.converterList.get(i);
            if (converter.responseAccept(itemEntity)) {
                value = converter.convert(itemEntity, value);
            }
        }
        return value;
    }


}
