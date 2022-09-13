package com.wisely.ucenter.service;

import com.wisely.framework.entity.PageVo;
import com.wisely.ucenter.entity.UcenterLog;
import com.github.pagehelper.PageInfo;

/**
 * 系统日志(UcenterLog)表服务接口
 *
 * @author xintao.li
 * @since 2021-06-25 17:58
 */
public interface UcenterLogService {

    /**
     * 分页查询
     *
     * @return
     */
    PageInfo findPage(UcenterLog query, PageVo pageVo);

    /**
     * 新增
     * @param ucenterLog UcenterLog
     * @return
     */
    Integer add(UcenterLog ucenterLog);
}
