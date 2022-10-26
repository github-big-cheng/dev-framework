package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.dictionary.ConverterDictionary;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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


        // apply multi keys
        AtomicReference<Object> reference = new AtomicReference<>(o);
        convertDictionaryList.forEach(dictionary -> {
            if (!dictionary.accept(item)) {
                return;
            }

            Object temp = dictionary.loadValue(item, reference.get());
            reference.set(temp);
        });

        return reference.get();
    }
}
