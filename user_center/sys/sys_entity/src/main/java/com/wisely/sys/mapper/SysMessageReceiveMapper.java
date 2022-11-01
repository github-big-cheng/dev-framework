package com.wisely.sys.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.sys.entity.SysMessageReceive;

/**
 * 消息接收表(SysMessageReceive)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-31 10:42:02
 */
public interface SysMessageReceiveMapper extends BaseMapper<SysMessageReceive> {

    void batchUpdateAsUnread(String idQueryIn);

    void batchUpdateAsRead(String idQueryIn);

    int batchDelete(String idQueryIn);

}
