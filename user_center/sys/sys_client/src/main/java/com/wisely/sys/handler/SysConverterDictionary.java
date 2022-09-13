package com.wisely.sys.handler;

import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.eum.SysCacheEnum;


public class SysConverterDictionary implements ConverterDictionary {

    @Override
    public boolean accept(String bizType) {
        return ValidHelper.isNotEmpty(SysCacheEnum.loadByMapper(bizType));
    }

    @Override
    public String loadValue(String bizKey, String valueKey, String defaultValue) {

        if (ValidHelper.isNotEmpty(bizKey) && ValidHelper.isNotEmpty(valueKey)) {
            String result = SysDictionaryHelper.loadEntityCacheManager(bizKey, valueKey);
            if (ValidHelper.isNotEmpty(result)) {
                return result;
            }
        }
        return defaultValue;
    }
}
