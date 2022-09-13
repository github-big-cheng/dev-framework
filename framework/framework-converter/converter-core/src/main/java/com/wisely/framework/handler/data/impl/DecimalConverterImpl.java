package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 精整型数值转换器
 * 1. required
 * 2. min
 * 3. max
 * 4. between
 */
public class DecimalConverterImpl implements DefaultDataConverter<BigDecimal>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_DECIMAL);
    }


    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        if (ValidHelper.isNull(o)) {
            item.validRequired();
            return o;
        }

        BigDecimal v = DataHelper.getBigDecimal(o);

        // 最小值
        String min = item.getExtendField(VALIDA_ATTR_MIN);
        if (ValidHelper.isNotBlank(min)) {
            AssertHelper.EX_VALIDATION.isTrue(DataHelper.getBigDecimal(min).compareTo(v) <= 0,
                    "converter.less_than_min_val.min.{0}.val.{1}", min, v);
        }

        // 最大值
        String max = item.getExtendField(VALIDA_ATTR_MAX);
        if (ValidHelper.isNotBlank(max)) {
            AssertHelper.EX_VALIDATION.isTrue(DataHelper.getBigDecimal(max).compareTo(v) >= 0,
                    "converter.great_than_max_val.max_{0}&val_{1}", max, v);
        }

        // 区间
        String between = item.getExtendField(VALIDA_ATTR_BETWEEN);
        if (ValidHelper.isNotBlank(between)) {
            List<String> dual = StringHelper.splitToList(between, "|");
            dual.forEach(x -> {
                String[] arr = x.split(",");
                boolean flag = ValidHelper.isBetween(v, DataHelper.getBigDecimal(arr[0]), DataHelper.getBigDecimal(arr[1]));
                AssertHelper.EX_VALIDATION.isTrue(flag,
                        "converter.out_of_range.range.{0}.val.{1}", x, v);
            });
        }
        return v;
    }

    @Override
    public BigDecimal convert(ConverterItemEntity item, Object o) {
        if (ValidHelper.isNull(o)) {
            return null;
        }
        int scale = item.getScale() == null ? 0 : item.getScale();
        return DataHelper.getBigDecimal(o).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }
}
