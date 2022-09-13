package com.wisely.sso.client.handler;

import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64DataConverterImpl implements DataConverter<String> {

    final static String KEY = "base64";

    @Override
    public boolean requestAccept(ConverterItemEntity item) {
        return DataHelper.getBoolean(item.getExtendField(KEY));
    }

    @Override
    public Object validation(ConverterItemEntity item, Object o) {
        String value = (String) o;
        if (ValidHelper.isBlank(value)) {
            return value;
        }
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }


    /**
     * 暂不考虑返回
     *
     * @param item
     * @param o
     * @return
     */
    @Override
    public String convert(ConverterItemEntity item, Object o) {

        String value = (String) o;
        if (ValidHelper.isBlank(value)) {
            return value;
        }

        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}
