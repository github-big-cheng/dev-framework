package com.wisely.ucenter.service;

import com.wisely.framework.entity.PageVo;
import com.wisely.sso.client.entity.SsoUser;
import com.wisely.ucenter.entity.UcenterUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 人员(TUcenterPerson)表服务接口
 *
 * @author xintao.li
 * @since 2021-05-31 10:00:05
 */
public interface UcenterUserService {

    /**
     * 条件列表查询
     *
     * @param query UcenterUser
     * @return List<UcenterUser>
     */
    List<UcenterUser> list(UcenterUser query);

    /**
     * 分页列表查询
     *
     * @param query  UcenterUser
     * @param pageVo PageVo
     * @return PageInfo
     */
    PageInfo findByPage(UcenterUser query, PageVo pageVo);

    /**
     * 保存
     *
     * @param record UcenterUser
     * @return int
     */
    int save(UcenterUser record);

    /**
     * 单条记录查询
     *
     * @param id id
     * @return UcenterUser
     */
    UcenterUser load(Integer id);

    /**
     * 删除
     *
     * @param id id
     * @return int
     */
    int delete(Integer id);

    /**
     * 根据personId获取UcenterUser
     *
     * @param personId personId
     * @return UcenterUser
     */
    UcenterUser loadByPersonId(Integer personId);

    /**
     * 修改UcenterUser
     *
     * @param ucenterUser UcenterUser
     */
    void updateByPrimaryKeySelective(UcenterUser ucenterUser);


    /**
     * 根据账号查找UcenterUser
     *
     * @param account 账号
     * @return UcenterUser
     */
    UcenterUser loadByAccount(String account);

    /**
     * 动态修改
     * @param ucenterUser UcenterUser
     */
    void updateBySelective(UcenterUser ucenterUser);


    SsoUser loadSsoUser();

    /**
     * 供删除账号使用
     * @param idQuery
     * @return
     */
    int deleteByIdQuery(String idQuery);
}
