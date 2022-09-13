package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.framework.entity.Model;
import com.wisely.ucenter.entity.UcenterObjFunc;

import java.util.List;

/**
 * 对象权限(UcenterObjFunc)表数据库访问层
 *
 * @author xi.wang
 * @since 2021-07-22 10:05:01
 */
public interface UcenterObjFuncMapper extends BaseMapper<UcenterObjFunc> {

    List<Integer> selectFuncIdsByPersonId(Model params);

    void deleteBySelective(UcenterObjFunc record);
}
