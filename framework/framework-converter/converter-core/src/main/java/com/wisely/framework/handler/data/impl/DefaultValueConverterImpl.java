package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.RandomHelper;
import com.wisely.framework.helper.ValidHelper;
import com.google.common.collect.Sets;

import java.util.Set;


/**
 * 默认值处理器
 *      暂仅接受输出转换
 */
public class DefaultValueConverterImpl implements DefaultDataConverter<Object>, ConverterConstants {

    @Override
    public boolean responseAccept(ConverterItemEntity item) {
        return item.getExtendField().isNotBlank(CONVERTER_ATTR_DEF);
    }

    @Override
    public Object convert(ConverterItemEntity item, Object o) {

        if (ValidHelper.isNull(o)) {
            String defVal = item.getDefaultVal();

            // 处理特殊逻辑
            if (SPECIAL_DEF_VAL.contains(defVal)) {
                switch (defVal) {
                    case "now()":
                        // 默认格式 yyyy-MM-dd HH:mm:ss
                        defVal = ValidHelper.isNotBlank(item.getToPattern()) ? DateHelper.formatNow(item.getToPattern()) : DateHelper.formatNow();
                        break;
                    case "random()":
                        // 默认32位，最大32位
                        defVal = item.getMaxlength() != null ? RandomHelper.uuid(item.getMaxlength()) : RandomHelper.uuid();
                        break;
                }
            }

            o = defVal;
        }

        return o;
    }


    private final static Set<String> SPECIAL_DEF_VAL =
            Sets.newHashSet(
                    "now()", // 当前时间
                    "random()" // 随机数
            );
}
