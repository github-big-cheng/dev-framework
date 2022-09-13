package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 布尔类型转换器
 */
@Slf4j
public class BooleanConverterImpl implements DefaultDataConverter<Boolean>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_BOOLEAN);
    }

    @Override
    public Object validation(ConverterItemEntity item, Object o) {
        return DataHelper.getBoolean(o);
    }

    @Override
    public Boolean convert(ConverterItemEntity item, Object o) {
        return DataHelper.getBoolean(o);
    }

}
