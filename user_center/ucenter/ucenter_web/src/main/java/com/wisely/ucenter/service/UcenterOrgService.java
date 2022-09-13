package com.wisely.ucenter.service;

import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterOrg;

import java.util.List;

/**
 * 机构/部门信息(TUcenterOrg)表服务接口
 *
 * @author xintao.li
 * @since 2021-05-31 10:24:05
 */
public interface UcenterOrgService {

    /**
     * 条件列表查询
     *
     * @param query UcenterOrg
     * @return List<UcenterOrg>
     */
    List<Model> findList(UcenterOrg query);

    /**
     * 分页列表查询
     *
     * @param query  UcenterOrg
     * @param pageVo PageVo
     * @return PageInfo
     */
    PageInfo findByPage(UcenterOrg query, PageVo pageVo);


    /**
     * 保存
     *
     * @param record UcenterOrg
     * @return int
     */
    int save(UcenterOrg record);

    /**
     * 单条记录查询
     *
     * @param id id
     * @return
     */
    Model load(Integer id);

    /**
     * 批量删除
     *
     * @param idQueryIn idQueryId 多个id  逗号分隔
     * @return int
     */
    int delete(String idQueryIn);

    /**
     * 显示树状部门
     *
     * @param ucenterOrg
     * @return
     */
    List<Model> tree(UcenterOrg ucenterOrg);

    /**
     * 查询部门以及部门下的人员
     *
     * @param ucenterOrg
     * @return
     */
    List<Model> deptPersonTree(UcenterOrg ucenterOrg);

    List<Model> deptPositionTree(UcenterOrg query);
}
