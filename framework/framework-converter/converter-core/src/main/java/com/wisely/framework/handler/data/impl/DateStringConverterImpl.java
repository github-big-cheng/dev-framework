package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.exception.SystemException;
import com.wisely.framework.exception.ValidationException;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 日期字符串转换器
 */
@Slf4j
public class DateStringConverterImpl implements DefaultDataConverter<String>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_DATESTRING);
    }

    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        String val = DataHelper.getString(o);

        if (ValidHelper.isBlank(val)) {
            item.validRequired();
            return o;
        }

        // 日期格式检查
        if (ValidHelper.isNotBlank(item.getFromPattern())) {
            try {
                DateHelper.parseDate(val, item.getFromPattern());
            } catch (Exception e) {
                throw ValidationException.builder("converter.invalid_date_pattern.pattern.{0}.val.{1}", e, item.getFromPattern(), val);
            }
        }

        // 最小值
        String min = item.getExtendField(VALIDA_ATTR_MIN);
        if (ValidHelper.isNotBlank(min)) {
            AssertHelper.EX_VALIDATION.isTrue(val.compareTo(min) >= 0,
                    "converter.less_than_min_val.min.{0}.val.{1}", min, val);
        }

        // 最大值
        String max = item.getExtendField(VALIDA_ATTR_MAX);
        if (ValidHelper.isNotBlank(max)) {
            AssertHelper.EX_VALIDATION.isTrue(val.compareTo(max) <= 0,
                    "converter.great_than_max_val.max_{0}&val_{1}", max, val);
        }

        String between = item.getExtendField(VALIDA_ATTR_BETWEEN);
        if (ValidHelper.isNotBlank(between)) {
            List<String> betweenList = StringHelper.splitToList(between, "|");
            betweenList.forEach(b -> {
                String[] arr = b.split(",");
                AssertHelper.EX_VALIDATION.isTrue(val.compareTo(arr[0]) >= 0 && val.compareTo(arr[1]) <= 0,
                        "converter.out_of_range.range_{0}&val_{1}", between, val);
            });
        }

        return val;
    }

    @Override
    public String convert(ConverterItemEntity item, Object o) {
        if (ValidHelper.isNull(o)) {
            return null;
        }
        Date d;
        try {
            if (StringHelper.isNotBlank(item.getFromPattern())) {
                d = DateHelper.getDate(o, item.getFromPattern());
            } else {
                d = DateHelper.getDate(o);
            }

            // 默认 yyyy-MM-dd HH:mm:ss
            String toPattern =
                    ValidHelper.isNotBlank(item.getToPattern()) ?
                            item.getToPattern() : DateHelper.PATTERN_DATETIME_01;
            if (d != null) {
                return DateHelper.format(d, toPattern);
            }
        } catch (Exception e) {
            throw SystemException.builder(e, "converter.data_parse_failed.{0}", o);
        }

        return null;
    }

}
