package com.wisely.ucenter.client.handler;

import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.eum.UcenterCacheEnum;


public class UcenterConverterDictionary implements ConverterDictionary {

    @Override
    public boolean accept(String bizType) {
        return ValidHelper.isNotEmpty(UcenterCacheEnum.loadByMapper(bizType));
    }

    @Override
    public String loadValue(String bizKey, String valueKey, String defaultValue) {

        if (ValidHelper.isNotEmpty(bizKey) && ValidHelper.isNotEmpty(valueKey)) {
            String result = UcenterDictionaryHelper.loadEntityCacheManager(bizKey, valueKey);
            if (ValidHelper.isNotEmpty(result)) {
                return result;
            }
        }
        return defaultValue;
    }
}
