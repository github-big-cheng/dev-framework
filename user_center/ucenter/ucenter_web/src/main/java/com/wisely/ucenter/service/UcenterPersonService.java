package com.wisely.ucenter.service;


import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterUser;

import java.util.List;

/**
 * 人员(TUcenterPerson)表服务接口
 *
 * @author xintao.li
 * @since 2021-05-28 18:00:55
 */
public interface UcenterPersonService {

    /**
     * 分页列表查询
     *
     * @param query  UcenterPerson
     * @param pageVo PageVo
     * @return PageInfo
     */
    Model findByPage(UcenterPerson query, PageVo pageVo);


    /**
     * 人员详细信息查询
     *
     * @param dataList
     * @return
     */
    List<Model> selectPersonWithDetail(List<UcenterPerson> dataList);

    /**
     * 保存
     *
     * @param record UcenterPerson
     * @return int
     */
    int save(UcenterPerson record);

    /**
     * 单条记录查询
     *
     * @param id id
     * @return UcenterPerson
     */
    Model load(Integer id);

    /**
     * 批量删除
     *
     * @param idQueryIn 多个id,逗号分隔
     * @return int
     */
    int delete(String idQueryIn);

    /**
     * 重置密码
     *
     * @param account 账号
     */
    void resetPwd(String account);


    /**
     * 账号操作
     * 锁定/解锁
     *
     * @param accounts
     * @param accountStatus
     */
    void accountOperation(String accounts, int accountStatus);

    /**
     * 修改头像
     *
     * @param ucenterPersonVo
     */
    void personImgSave(UcenterPerson ucenterPersonVo);

    /**
     * 获取指定角色的人员(部门可选)
     *
     * @param query
     * @return
     */
    List<Model> loadPersonBySelective(UcenterPerson query);

    /**
     * 修改密码
     */
    void updatePassword(UcenterUser user, String newPassword);
}
