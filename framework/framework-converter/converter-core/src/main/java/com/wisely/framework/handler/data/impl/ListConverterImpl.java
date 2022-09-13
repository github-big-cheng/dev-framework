package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.handler.operation.ConverterOperation;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.List;

public class ListConverterImpl implements ConverterConstants, DefaultDataConverter<Object> {


    @Lazy // 循环引用，使用懒加载
    @Resource
    ConverterOperation converterOperation;

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_LIST);
    }


    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        // 必输校验
        if (ValidHelper.isEmpty(o)) {
            item.validRequired();
            return o;
        }

        // 数据转换
        List<Model> list = DataHelper.getModelList(o);

        if (ValidHelper.isNotEmpty(item.getMaxlength())) {
            AssertHelper.EX_VALIDATION.isTrue(
                    ValidHelper.isBetween(list.size(), 0, item.getMaxlength()),
                    "common.out_of_maxlength.{0}.{1}",
                    item.getName(),
                    item.getMaxlength());
        }

        if (ValidHelper.isNotEmpty(item.getChildren())) {
            list.forEach(data ->
                    item.getChildren().forEach(children -> {
                        Object value = this.converterOperation.validation(children, data.get(children.getKey()));
                        data.set(children.getName(), value);
                    })
            );
        }

        return list;
    }


    @Override
    public Object convert(ConverterItemEntity item, Object o) {

        // 全部返回
        if (DataHelper.getBoolean(item.getExtendField(ATTR_PUT_ALL))) {
            // 对set、数组等的支持
            return o;
        }

        // 转换数据为list
        List<Model> list = DataHelper.getModelList(o);

        List<Model> tempList = Lists.newArrayList();
        // 无返回配置或数据为空
        if (ValidHelper.isEmpty(item.getChildren()) || ValidHelper.isEmpty(list)) {
            return tempList;
        }

        list.forEach(data -> { // 为保证数据顺序，这里用串行流
            Model temp = Model.builder();
            tempList.add(temp);
            item.getChildren().forEach(children -> {
                Object value = converterOperation.converter(children, data.get(children.getKey()));
                temp.set(children.getName(), value);
            });
        });

        return tempList;
    }
}
