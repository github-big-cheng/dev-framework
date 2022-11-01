package com.wisely.sys.service;

import com.wisely.sys.entity.SysMessage;

import java.util.List;

public interface SysMsgService {

    int delete(String idQueryIn);

    List<SysMessage> selectList(SysMessage record);
}
