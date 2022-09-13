package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 字典转换器
 */
@Slf4j
public class MapperConverterImpl implements DefaultDataConverter<Object>, ConverterConstants {

    @Autowired(required = false)
    List<ConverterDictionary> convertDictionaryList;

    @Override
    public boolean responseAccept(ConverterItemEntity item) {
        return item.getExtendField().isNotBlank(CONVERTER_ATTR_MAPPER);
    }

    @Override
    public Object convert(ConverterItemEntity item, Object o) {

        if (ValidHelper.isEmpty(convertDictionaryList)) {
            log.warn("Dictionary Bean {} not found in Spring Context", "com.dounion.framework.handler.dictionary.ConvertDictionary");
            return o;
        }
        if (StringHelper.isBlank(item.getMapper())) {
            return o;
        }

        // 待转换数据数组
        String[] values = StringHelper.split(DataHelper.getString(o, null),
                item.getExtendField().getString(CONVERTER_ATTR_SEPARATOR,","));
        if (ValidHelper.isEmpty(values)) {
            return o;
        }

        // support multi keys
        List<String> list = Lists.newArrayList();
        for (ConverterDictionary dictionary : convertDictionaryList) {

            if (!dictionary.accept(item.getMapper())) {
                continue;
            }

            for (String val : values) {
                list.add(dictionary.loadValue(item.getMapper(), val, item.getDefaultVal()));
            }
        }
        return StringHelper.join(list, item.getExtendField().getString(CONVERTER_ATTR_SEPARATOR,","));
    }
}
