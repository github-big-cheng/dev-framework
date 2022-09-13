package com.wisely.ucenter.entity;

import java.io.Serializable;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户中心数据映射表(UcenterMapping)实体类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:32:23
 */
@Setter
@Getter
public class UcenterMapping extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 334665969348033024L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 机构ID
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 映射对应的本库表的id 例如往来单位的id
     */
    private Integer mapId;
    /**
     * 映射的基础数据类型 往来单位、部门
     */
    private String mapType;
    /**
     * 第三方代码
     */
    private String fromMapCode;
    /**
     * 第三方名称
     */
    private String fromMapName;
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

}
