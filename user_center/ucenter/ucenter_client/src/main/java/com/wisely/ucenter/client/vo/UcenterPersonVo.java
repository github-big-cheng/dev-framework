package com.wisely.ucenter.client.vo;

import com.wisely.framework.entity.BaseEntity;
import com.wisely.framework.handler.cache.EntityCacheManager;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UcenterPersonVo extends BaseEntity implements Serializable, EntityCacheManager {

    private static final long serialVersionUID = -54739022558305992L;
    /**
     * ID ID
     */
    private Integer id;
    /**
     * 机构ID 为系统后续多机构新增字段
     */
    private Integer orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 代码
     */
    private String code;
    /**
     * 编号(工号）
     */
    private String billNo;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别 TYPE = 10004
     */
    private String sex;
    /**
     * 职务
     */
    private Integer posId;
    /**
     * 职级 TYPE = 10080
     */
    private String posLevel;
    /**
     * 婚姻状况 TYPE = 10005
     */
    private String marriage;
    /**
     * 身份证号码
     */
    private String idNo;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 民族
     */
    private String nation;
    /**
     * 籍贯省份
     */
    private String homeProvince;
    /**
     * 籍贯城市
     */
    private String homeCity;
    /**
     * 籍贯县 TYPE = 10007
     */
    private String homeCounty;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 电话
     */
    private String telephone;
    /**
     * email地址
     */
    private String email;
    /**
     * 邮编
     */
    private String zipcode;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 学历 TYPE = 10006
     */
    private String education;
    /**
     * 学位
     */
    private String degree;
    /**
     * 毕业时间
     */
    private String graduateTime;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 专业
     */
    private String major;
    /**
     * 参加工作时间
     */
    private String beginWorkTime;
    /**
     * 入职时间
     */
    private String workTime;
    /**
     * 员工状态
     */
    private String personState;
    /**
     * 顺序号
     */
    private Integer orderNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * IMSI号码
     */
    private String imsiNo;
    /**
     * IMEI号码
     */
    private String imeiNo;


    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 账号状态 1- 正常   2-冻结(锁定)   3-注销
     */
    private Integer accountStatus;
    /**
     * 人员账号
     */
    private String account;
}
