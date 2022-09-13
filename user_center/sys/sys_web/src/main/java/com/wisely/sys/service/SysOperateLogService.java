package com.wisely.sys.service;

import com.wisely.sys.entity.SysOperateLog;
import com.wisely.framework.entity.PageVo;
import com.github.pagehelper.PageInfo;

public interface SysOperateLogService {

    /**
     * 分页查询
     *
     * @return
     */
    PageInfo findPage(SysOperateLog query, PageVo pageVo);

    /**
     * 新增
     * @param sysOperateLog SysOperateLog
     * @return
     */
    Integer add(SysOperateLog sysOperateLog);
}
