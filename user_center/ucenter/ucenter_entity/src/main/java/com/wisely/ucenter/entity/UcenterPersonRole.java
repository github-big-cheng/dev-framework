package com.wisely.ucenter.entity;

import java.io.Serializable;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色人员中间表(UcenterPersonRole)实体类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:33:14
 */
@Setter
@Getter
public class UcenterPersonRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 258750544617231771L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 人员
     */
    private Integer personId;
    /**
     * 角色
     */
    private Integer roleId;
    /**
     * 操作状态
     */
    private Integer opState;
    /**
     * 录入人
     */
    private Integer createBy;
    /**
     * 录入时间
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
     * 删除标记
     */
    private Integer isDeleted;

    //扩展字段
    /**
     * 批量人员id   逗号分隔
     */
    private String personIdQueryIn;

}