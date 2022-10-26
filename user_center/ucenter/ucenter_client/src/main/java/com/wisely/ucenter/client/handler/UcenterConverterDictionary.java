package com.wisely.ucenter.client.handler;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.eum.UcenterCacheEnum;


public class UcenterConverterDictionary implements ConverterDictionary<String>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity itemEntity) {
        return ValidHelper.isNotEmpty(UcenterCacheEnum.loadByMapper(itemEntity.getMapper()));
    }

    @Override
    public String loadValue(ConverterItemEntity itemEntity, Object value) {

        String result = DataHelper.getString(value);
        if (StringHelper.isBlank(result)) {
            return result;
        }

        return UcDictHelper.loadValue(itemEntity.getMapper(), result);
    }
}
