package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数表(SysParameter)实体类
 *
 * @author system
 * @since 2022-09-05 11:02:09
 */
@Setter
@Getter
public class SysParameter extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 228752285869322926L;
    /**
    * id
    */
    private Integer id;
    /**
    * 机构id
    */
    private Integer orgId;
    /**
    * 机构名称
    */
    private String orgName;
    /**
    * key
    */
    private String keies;
    /**
    * value
    */
    private String value;
    /**
    * 描述
    */
    private String description;
    /**
    * 参数类型
    */
    private String type;
    /**
    * 参数子类型
    */
    private String subType;
    /**
    * 备注
    */
    private String remark;
    /**
    * 排序
    */
    private Integer orderNo;
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


    /*-------扩展字段---------*/
    /**
     * ids
     */
    private String idQueryIn;
    /**
     * ids
     */
    private String keiesQueryIn;
    /**
     * key模糊查询
     */
    private String keiesQueryLike;
    /**
     * 说明模糊查询
     */
    private String descriptionQueryLike;
    /**
     * orgId集合
     */
    private String orgIdQueryIn;
}
