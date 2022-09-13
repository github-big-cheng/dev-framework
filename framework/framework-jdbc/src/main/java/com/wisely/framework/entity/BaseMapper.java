package com.wisely.framework.entity;

import java.util.List;

public interface BaseMapper<E extends BaseEntity> {


    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    E selectByPrimaryKey(Object id);


    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Object id);

    /**
     * 插入
     *
     * @param record
     * @return
     */
    int insert(E record);

    /**
     * 动态插入
     *
     * @param record
     * @return
     */
    int insertSelective(E record);

    /**
     * 按主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(E record);


    /**
     * 按主键动态更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(E record);


    /**
     * 动态条件更新
     *
     * @param record
     * @return
     */
    int updateBySelective(E record);

    /**
     * 条件列表查询
     *
     * @param query
     * @return
     */
    List<E> selectListBySelective(E query);

}
