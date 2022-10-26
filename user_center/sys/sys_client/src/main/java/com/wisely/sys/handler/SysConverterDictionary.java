package com.wisely.sys.handler;

import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.eum.SysCacheEnum;


public class SysConverterDictionary implements ConverterDictionary<String>, SysConstants {

    @Override
    public boolean accept(ConverterItemEntity itemEntity) {
        return SysCacheEnum.getMapperModel().containsKey(itemEntity.getMapper());
    }

    @Override
    public String loadValue(ConverterItemEntity itemEntity, Object value) {

        String result = DataHelper.getString(value);
        if (StringHelper.isBlank(result)) {
            return result;
        }

        if (StringHelper.equals(itemEntity.getMapper(), CODE_MAPPER_KEY)) {
            return SysDictHelper.loadValue(itemEntity.getMapper(), result, itemEntity.getExtendField("locale"));
        }

        return SysDictHelper.loadValue(itemEntity.getMapper(), result);
    }
}
