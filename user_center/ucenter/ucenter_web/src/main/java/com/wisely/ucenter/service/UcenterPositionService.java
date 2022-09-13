package com.wisely.ucenter.service;

import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterPosition;
import com.github.pagehelper.PageInfo;

public interface UcenterPositionService {

    PageInfo findByPage(UcenterPosition query, PageVo pageVo);

    int save(UcenterPosition record);

    UcenterPosition load(Integer id);

    int delete(String ids);
}
