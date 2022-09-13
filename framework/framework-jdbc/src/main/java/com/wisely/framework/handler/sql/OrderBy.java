package com.wisely.framework.handler.sql;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderBy {

    /**
     * 正序
     * false为倒序
     *
     * @return
     */
    private boolean asc = true;

    /**
     * 数据库对应字段
     *
     * @return
     */
    private String column;

    /**
     * BaseEntity.orderBy对应的key
     *
     * @return
     */
    private String key;

    /**
     * 表的别名
     * ex: T
     *
     * @return
     */
    private String alias = "";

    /**
     * 是否检查字段为null
     * 为true时调用SqlDialect中的getIfNull方法
     *
     * @return
     */
    private boolean checkNull = false;

    /**
     * 查询为null时的默认值
     * 具体语句根据方言生成
     * 具体类型根据valueType生成
     * ifnull(null, nullValue) or nvl(null, nullValue)
     *
     * @return
     */
    private String nullValue = "";

    /**
     * 生成sql时的值
     * String加 ”'“
     *
     * @return
     */
    private Class valueType = String.class;
}
