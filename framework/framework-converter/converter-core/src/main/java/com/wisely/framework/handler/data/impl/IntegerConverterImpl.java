package com.wisely.framework.handler.data.impl;

import com.wisely.framework.common.ConverterConstants;
import com.wisely.framework.handler.data.DefaultDataConverter;
import com.wisely.framework.handler.entity.ConverterItemEntity;
import com.wisely.framework.helper.AssertHelper;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;

import java.util.List;
import java.util.Set;

/**
 * 整型转换器
 */
public class IntegerConverterImpl implements DefaultDataConverter<Integer>, ConverterConstants {

    @Override
    public boolean accept(ConverterItemEntity item) {
        return ValidHelper.isEquals(item.getType(), DATA_TYPE_INTEGER);
    }

    /**
     * 整形校验
     * 1. required 必输
     * 2. min 最小值
     * 3. max 最大值
     * 4. between 范围
     * 5. scope
     *
     * @param item
     * @param o
     */
    @Override
    public Object validation(ConverterItemEntity item, Object o) {

        if (ValidHelper.isBlank(DataHelper.getString(o))) {
            item.validRequired();
            return o;
        }

        Integer v = DataHelper.getInt(o);

        // 最小值
        String min = item.getExtendField(ConverterConstants.VALIDA_ATTR_MIN);
        if (ValidHelper.isNotBlank(min)) {
            AssertHelper.EX_VALIDATION.isTrue(v >= DataHelper.getInt(min),
                    "converter.less_than_min_val.min.{0}.val.{1}", min, v);
        }

        // 最大值
        String max = item.getExtendField(ConverterConstants.VALIDA_ATTR_MAX);
        if (ValidHelper.isNotBlank(max)) {
            AssertHelper.EX_VALIDATION.isTrue(v <= DataHelper.getInt(max),
                    "converter.great_than_max_val.max_{0}&val_{1}", max, v);
        }

        // 区间
        String between = item.getExtendField(ConverterConstants.VALIDA_ATTR_BETWEEN);
        if (ValidHelper.isNotBlank(between)) {
            List<String> dual = StringHelper.splitToList(between, "|");
            dual.forEach(x -> {
                String[] arr = x.split(",");
                boolean flag = ValidHelper.isBetween(v, DataHelper.getLong(arr[0]), DataHelper.getLong(arr[1]));
                AssertHelper.EX_VALIDATION.isTrue(flag,
                        "converter.out_of_range.range_{0}&val_{1}", x, v);
            });
        }

        // 范围
        String scope = item.getExtendField(ConverterConstants.VALIDA_ATTR_SCOPE);
        if (ValidHelper.isNotBlank(scope)) {
            Set<String> scopes = StringHelper.splitToSet(scope, ",");
            AssertHelper.EX_VALIDATION.isTrue(scopes.contains(DataHelper.getString(v)),
                    "converter.out_of_scope.{0}.{1}", item.getName(), scope);
        }
        return v;
    }

    @Override
    public Integer convert(ConverterItemEntity item, Object o) {
        return o == null ? null : DataHelper.getInt(o);
    }
}
