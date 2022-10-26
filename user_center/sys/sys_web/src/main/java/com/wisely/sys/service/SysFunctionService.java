package com.wisely.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.sys.entity.SysFunction;

import java.util.List;

/**
 * 功能(SysFunction)表服务接口
 *
 * @author system
 * @since 2021-05-28 17:42:02
 */
public interface SysFunctionService {

    /**
     * 分页列表查询
     *
     * @param query
     * @param pageVo
     * @return
     */
    PageInfo findByPage(SysFunction query, PageVo pageVo);


    /**
     * 保存
     *
     * @param record
     * @return
     */
    int save(SysFunction record);

    /**
     * 单条记录查询
     *
     * @param id
     * @return
     */
    Model view(Integer id);


    /**
     * 删除
     *
     * @param ids)
     * @return
     */
    int delete(String ids);

    /**
     * 含project的菜单树结构
     *
     * @param query
     * @return
     */
    List<Model> listTree(SysFunction query);
}