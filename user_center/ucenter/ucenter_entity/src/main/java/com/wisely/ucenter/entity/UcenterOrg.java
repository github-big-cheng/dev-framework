package com.wisely.ucenter.entity;

import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 机构/部门信息(UcenterOrg)实体类
 *
 * @author ruijie.hu
 * @since 2021-05-28 17:32:34
 */
@Setter
@Getter
public class UcenterOrg extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 151952996957546506L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 所属机关（单位）ID
     */
    private Integer orgId;

    /**
     * 所属机关（单位）名称
     */
    private String orgName;

    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 路径 ROOT_ID-10001-10002…
     */
    private String pathIds;
    /**
     * 路径名称 ROOT_ID-10001-10002…
     */
    private String pathNames;

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String cname;
    /**
     * 英文名称
     */
    private String ename;
    /**
     * 简称
     */
    private String sname;
    /**
     * 企业/部门联系人 省级平台规范：企业联系人
     */
    private String linker;
    /**
     * 联系人id
     */
    private Integer linkId;
    /**
     * 联系人手机号
     */
    private String linkerMobilePhone;
    /**
     * 办公室电话
     */
    private String telephone;
    /**
     * 传真
     */
    private String fax;

    /**
     * 顺序号
     */
    private Integer orderNo;
    /**
     * 类型 部门/机构 10027-10:部门, 10027-20:党支部, 10027-30:机构
     */
    private String compType;
    /**
     * 备注
     */
    private String memo;
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
     * 更新人
     */
    private Integer updateBy;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 删除标记
     */
    private Integer isDeleted;


    /* 扩展字段 */
    /**
     * 批量id  逗号分隔
     */
    private String idQueryIn;
    /**
     * 指定多类型查询
     */
    private String compTypeQueryIn;
    /**
     * 父亲id集合
     */
    private String parentIdQueryIn;
    /**
     * 名称,代码,简称,联系人模糊查询
     */
    private String cNameOrCodeOrSnameOrLinkerQueryLike;

    /**
     * 查询机构或者该机构下的子部门
     */
    private String idOrOrgIdQueryIn;
    /**
     * 路径 模糊搜索
     */
    private String pathIdsQueryLike;
    /**
     * 部门名称模糊搜索
     */
    private String cnameQueryLike;
}
