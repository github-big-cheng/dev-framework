package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 功能菜单表(SysFunction)实体类
 *
 * @author system
 * @since 2022-09-05 10:52:13
 */
@Setter
@Getter
public class SysFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 754971373893123543L;
    /**
    * id
    */
    private Integer id;
    /**
    * 菜单名称
    */
    private String name;
    /**
    * 资源路径
    */
    private String action;
    /**
    * 父级id
    */
    private Integer parentId;
    /**
    * 代码
    */
    private String code;
    /**
    * 菜单类型 1-菜单 2-子菜单 3-tab页签 4-按钮
    */
    private Integer type;
    /**
    * 功能简介
    */
    private String description;
    /**
    * 图片地址
    */
    private String imgPath;
    /**
    * 排序号
    */
    private Integer orderNo;
    /**
    * 所属项目id
    */
    private Integer projectId;
    /**
    * 是否手机端菜单
    */
    private Integer isPhone;
    /**
    * 手机类型
    */
    private Integer phoneType;
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


    /*******扩展字段*********/
    /**
     * 多id查询
     */
    private String idQueryIn;
    /**
     * 名称模糊查询
     */
    private String nameQueryLike;

}
