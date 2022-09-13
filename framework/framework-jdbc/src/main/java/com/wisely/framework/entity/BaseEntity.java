package com.wisely.framework.entity;

import com.wisely.framework.handler.sql.OrderByCache;
import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.helper.ValidHelper;

public class BaseEntity {


    /**
     * 支持json格式打印对象信息
     *
     * @return
     */
    @Override
    public String toString() {
        return JsonHelper.obj2Json(this);
    }


    private String orderBy;

    public String getOrderBy() {
        if (ValidHelper.isBlank(this.orderBy)) {
            return null;
        }
        return OrderByCache.getOrderBySql(this.getClass(), this.orderBy);
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
