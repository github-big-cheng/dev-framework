package com.wisely.framework.handler.sql.impl;

import com.wisely.framework.handler.sql.SqlDialect;

public class OracleDialect implements SqlDialect {

    @Override
    public String getSqlIfNull(String ex1, Class valueType, Object defaultVal) {
        return "NVL(" + ex1 + ", " + this.generateValue(valueType, defaultVal) + ")";
    }
}
