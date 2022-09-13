package com.wisely.sys.entity;

import java.io.Serializable;
import com.wisely.framework.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 附件表(SysFile)实体类
 *
 * @author system
 * @since 2022-09-05 10:58:12
 */
@Setter
@Getter
public class SysFile extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -79364396305008793L;
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
    * 上传时间
    */
    private String uploadTime;
    /**
    * 附件大小（字节）
    */
    private Long attachSize;
    /**
    * 业务类型
    */
    private String sourceType;
    /**
    * 业务子类型
    */
    private String subType;
    /**
    * 业务id
    */
    private Integer sourceId;
    /**
    * 文件名称
    */
    private String filename;
    /**
    * 保存路径
    */
    private String realPath;
    /**
    * 扩展名 jpg|gif|xls|doc|...
    */
    private String extension;
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


    /**
     * 扩展字段
     */
    private String sourceIdQueryIn;
}
