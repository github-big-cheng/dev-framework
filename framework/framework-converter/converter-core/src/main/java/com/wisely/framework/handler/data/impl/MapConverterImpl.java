package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.ConverterOperation;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

public class MapConverterImpl implements ConverterConstants, DefaultDataConverter<Object> {

    @Lazy // 循环引用，使用懒加载
    @Resource
    ConverterOperation converterOperation;


    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_MAP);
    }

    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        Model result = Model.parseObject(o);
        if (ValidHelper.isEmpty(result)) {
            item.validRequired();
            return result;
        }

        if (ValidHelper.isEmpty(result) ||
                DataHelper.getBoolean(item.getExtendField(ATTR_PUT_ALL)) ||
                ValidHelper.isEmpty(item.getChildren())
        ) {
            return result;
        }


        item.getChildren().forEach(children -> {
            Object value = converterOperation.validation(children, result.get(children.getKey()));
            result.set(children.getName(), value);
        });

        return result;
    }

    @Override
    public Object convert(ConverterItemEntity item, Object o) {

        Model result = Model.parseObject(o);
        if (DataHelper.getBoolean(item.getExtendField(ATTR_PUT_ALL))) {
            return result;
        }

        Model temp = Model.builder();
        if (ValidHelper.isEmpty(item.getChildren()) || ValidHelper.isEmpty(result)) {
            return temp;
        }

        // 随机key
        if (DataHelper.getBoolean(item.getExtendField(ATTR_RANDOM_KEY))) {
            return this.randomKey(item, result);
        }

        // ignore-key
        boolean flag = ValidHelper.isSize(item.getChildren(), 1)
                && DataHelper.getBoolean(item.getChildren().get(0).getExtendField(ATTR_IGNORE_KEY));
        if (flag) {
            ConverterItemEntity ignoreItem = item.getChildren().get(0);
            return converterOperation.converter(ignoreItem, o);
        }


        item.getChildren().forEach(children -> {
            // 随机key
            if (ValidHelper.isEquals(children.getType(), DATA_TYPE_MAP)
                    && DataHelper.getBoolean(children.getExtendField(ATTR_RANDOM_KEY))) {
                temp.putAll(this.randomKey(children, result));
                return;
            }

            Object value = converterOperation.converter(children, result.get(children.getKey()));
            temp.set(children.getName(), value);
        });


        return temp;
    }


    private Model randomKey(ConverterItemEntity item, Model result) {
        Model temp = Model.builder();

        String itemJson = JsonHelper.obj2Json(item);
        ConverterItemEntity copy = JsonHelper.json2Obj(itemJson, ConverterItemEntity.class);
        copy.setExtendField(ATTR_RANDOM_KEY, "false");

        result.keySet().forEach(resultKey -> {
            Object itemValue = result.get(resultKey);
            copy.setName(DataHelper.getString(resultKey));
            temp.set(resultKey, this.convert(copy, itemValue));
        });

        return temp;
    }
}
