package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统代码表(SysCode)实体类
 *
 * @author system
 * @since 2022-09-05 10:53:16
 */
@Setter
@Getter
public class SysCode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -99447933535201821L;
    /**
    * id
    */
    private Integer id;
    /**
    * id
    */
    private String value;
    /**
    * 类型
    */
    private String type;
    /**
    * 本地化
    */
    private String locale;
    /**
    * 路径
    */
    private String pathValue;
    /**
    * 代码值
    */
    private String code;
    /**
    * 名称
    */
    private String name;
    /**
    * 排序号
    */
    private Integer orderNo;
    /**
    * 父级
    */
    private String parent;
    /**
    * 代码类型 1-系统 0-用户
    */
    private Integer isSys;
    /**
    * 操作状态
    */
    private Integer opState;
    /**
    * 创建人
    */
    private Integer createBy;
    /**
    * 创建时间
    */
    private String createTime;
    /**
    * 修改人
    */
    private Integer updateBy;
    /**
    * 修改时间
    */
    private String updateTime;
    /**
    * 是否删除 0-否 1-是
    */
    private Integer isDeleted;


    /* 扩展字段 */
    /**
     * 多个主键value,逗号分隔
     */
    private String valueQueryIn;
    /**
     * 代码模糊查询
     */
    private String codeQueryLike;
    /**
     * 名称模糊查询
     */
    private String nameQueryLike;
    /**
     * 代码或者名称查询
     */
    private String codeOrNameQueryLike;
    /**
     * 代码路径模糊查询-用于树结构查询
     */
    private String pathValueQueryLike;
    /**
     * type is null
     */
    private String typeQueryIsNull;
    /**
     * 类型排除
     */
    private String typeQueryNotIn;
}
