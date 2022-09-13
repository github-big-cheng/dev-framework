package com.wisely.ucenter.service;

import com.wisely.ucenter.entity.UcenterObjFunc;

/**
 * ObjFuncService
 *
 * @author ruijie.hu
 * @since 2021-06-01 13:42:02
 */
public interface UcenterObjFuncService {
    /**
     * 权限查询
     * @param query
     * @return
     */
    Object list(UcenterObjFunc query);


    /**
     * 保存
     * @param record
     * @return
     */
    int save(UcenterObjFunc record);
}