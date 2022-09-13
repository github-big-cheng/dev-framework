package com.wisely.sso.client.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SsoFunction implements Serializable {

    private static final long serialVersionUID = 258137646927753070L;
    /**
     * ID ID为固定的值，程序初始化提供
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * ACTION
     */
    private String action;
    /**
     * 上级 父ID,ROOT的父ID为0
     */
    private Integer parentId;
    /**
     * 代码 唯一
     */
    private String code;
    /**
     * 排序号
     */
    private String orderBy;
}
