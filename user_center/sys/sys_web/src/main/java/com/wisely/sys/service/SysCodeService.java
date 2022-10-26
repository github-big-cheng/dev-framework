package com.wisely.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.sys.entity.SysCode;

import java.util.List;


public interface SysCodeService {


    /**
     * 动态查询
     *
     * @param query SysCode
     * @return List<SysCode>
     */
    List<SysCode> findList(SysCode query);


    /**
     * 分页模糊查询代码
     *
     * @param query  SysCode对象
     * @param pageVo 封装分页代码对象
     * @return PageInfo对象
     */
    PageInfo<SysCode> findPage(SysCode query, PageVo pageVo);

    /**
     * 新增代码
     *
     * @param record SysCode对象
     * @return String
     */
    String add(SysCode record);

    /**
     * 编辑系统代码信息
     *
     * @param record SysCode对象
     * @return String
     */
    String save(SysCode record);

    /**
     * 根据主键VALUE查询代码信息
     *
     * @param value 主键代码值
     * @return UcenterCode对象
     */
    SysCode load(Integer value);

    /**
     * 批量删除
     *
     * @param values
     * @return
     */
    int deleteByValues(String values);

    /**
     * 显示树状数据
     *
     * @param query 必填
     * @return
     */
    List<Model> tree(SysCode query);
}
