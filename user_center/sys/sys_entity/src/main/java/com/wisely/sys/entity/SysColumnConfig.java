package com.wisely.sys.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 自定义页面配置表(SysColumnConfig)实体类
 *
 * @author system
 * @since 2021-06-02 18:01:11
 */
@Setter
@Getter
public class SysColumnConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -34358229234512325L;
    /**
     * ID
     */
    private Integer id;
    /**
     * 用户ID T_UCENTER_USER 0-系统默认
     */
    private Integer userId;
    /**
     * 页面代码 T_UCENTER_FUNCTION.CODE
     */
    private String code;
    /**
     * 页面名称
     */
    private String name;
    /**
     * 字段key
     */
    private String colKey;
    /**
     * 字段名称
     */
    private String colName;
    /**
     * 列宽
     */
    private BigDecimal colWidth;
    /**
     * 行高
     */
    private BigDecimal height;
    /**
     * 是否选中
     */
    private Integer isSelected;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 操作状态
     */
    private Integer opState;
    /**
     * 创建人 T_UCENTER_USER
     */
    private Integer createBy;
    /**
     * 创建时间 yyyy-mm-dd hh:mm:ss
     */
    private String createTime;
    /**
     * 修改人 T_UCENTER_USER
     */
    private Integer updateBy;
    /**
     * 修改时间 yyyy-mm-dd hh:mm:ss
     */
    private String updateTime;
    /**
     * 是否删除 1-是 0-否
     */
    private Integer isDeleted;

}