package com.wisely.ucenter.service;

import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterRole;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 角色表(UcenterRole)服务接口
 * @author xi.wang
 * @date 2021/5/31 15:39
 */
public interface UcenterRoleService {

    /**
     * 条件列表查询
     * @param query
     * @return List<UcenterRole>
     */
    List<UcenterRole>  findList(UcenterRole query);

    /**
     * 分页查询
     * @param query  UcenterRole 对象
     * @param pageVo   VoPageVo 对象
     * @return  PageInfo对象
     */
    PageInfo<UcenterRole> findByPage(UcenterRole query, PageVo pageVo);


    /**
     * 单条记录查询
     * @param id
     * @return
     */
    UcenterRole load(Integer id);

    /**
     * 增/改
     * @param record  UcenterRole 对象
     * @return  int
     */
    int save(UcenterRole record);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int delete(String ids);

}
