package com.wisely.ucenter.service;

import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonOrg;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 部门/机构人员中间表(TUcenterPersonOrg)表服务接口
 *
 * @author xintao.li
 * @since 2021-05-31 10:41:52
 */
public interface UcenterPersonOrgService {

    /**
     * 条件列表查询
     *
     * @param query UcenterPersonOrg
     * @return List<UcenterPersonOrg>
     */
    List<UcenterPersonOrg> list(UcenterPersonOrg query);

    /**
     * 分页列表查询
     *
     * @param query  UcenterPersonOrg
     * @param pageVo PageVo
     * @return PageInfo
     */
    PageInfo findByPage(UcenterPersonOrg query, PageVo pageVo);


    /**
     * 按部门新增人员关系
     *
     * @param input
     * @return
     */
    int add(Model input);

    /**
     * 查看
     *
     * @param personId
     * @return
     */
    Model view(Integer personId);

    /**
     * 修改人员所属部门id以及是否主组织、是否重要部门负责人等信息
     *
     * @param deptInfoList  部门信息
     * @param ucenterPerson
     */
    void updatePersonOrgByList(List<Model> deptInfoList, UcenterPerson ucenterPerson);

    /**
     * 按人员删除关系
     *
     * @param personId
     */
    void deleteByPersonId(Integer personId);
}
