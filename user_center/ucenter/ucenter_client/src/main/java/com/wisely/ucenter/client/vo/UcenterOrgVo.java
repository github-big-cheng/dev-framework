package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UcenterOrgVo extends BaseEntity implements Serializable, EntityCacheManager {

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
     * 父级ID
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
    private String remark;
    /**
     * 扩展信息
     */
    private Model info = Model.builder();
}
