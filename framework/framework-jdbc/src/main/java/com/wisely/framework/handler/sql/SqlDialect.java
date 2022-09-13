package com.wisely.framework.handler.sql;

import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ValidHelper;

public interface SqlDialect {


    /**
     * NULL
     */
    String KEY_WORD_NULL = "NULL";

    /**
     * 正序
     */
    String KEY_WORD_ASC = "ASC";

    /**
     * 倒序
     */
    String KEY_WORD_DESC = "DESC";

    /**
     * 单引号
     */
    String SINGLE_QUOTATION = "'";


    default String generateValue(Class valueType, Object obj) {
        if (ValidHelper.isEmpty(obj)) {
            return KEY_WORD_NULL;
        }

        if (Number.class.isAssignableFrom(valueType)) {
            return DataHelper.getBigDecimal(obj).toString();
        }

        return SINGLE_QUOTATION + DataHelper.getString(obj) + SINGLE_QUOTATION;
    }

    /**
     * 获取IF_NULL语句
     *
     * @param ex1
     * @param defaultVal
     * @return
     */
    String getSqlIfNull(String ex1, Class valueType, Object defaultVal);
}
