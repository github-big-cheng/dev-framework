package com.wisely.framework.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageVo {

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 15;

}
