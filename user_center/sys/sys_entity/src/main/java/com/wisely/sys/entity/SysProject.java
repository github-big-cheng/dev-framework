package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 模块表(SysProject)实体类
 *
 * @author system
 * @since 2022-09-05 10:52:40
 */
@Setter
@Getter
public class SysProject extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -18019456151917694L;
    /**
    * id
    */
    private Integer id;
    /**
    * 模块名称
    */
    private String name;
    /**
    * 模块代码
    */
    private String code;
    /**
    * 排序号
    */
    private Integer orderNo;
    /**
    * 模块类型
    */
    private Integer type;
    /**
    * 模块路径
    */
    private String path;
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


    /*** 扩展字段 ***/
    /**
     * 批量删除
     */
    private String idQueryIn;

    private String nameQueryLike;

}
