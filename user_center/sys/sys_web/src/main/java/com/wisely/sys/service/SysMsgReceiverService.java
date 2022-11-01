package com.wisely.sys.service;

import com.wisely.framework.entity.Model;
import com.wisely.sys.entity.SysMessageReceive;

import java.util.List;

public interface SysMsgReceiverService {

    void markAsRead(Integer id);

    void markAsUnread(Integer id);

    void markSelectedAsRead(String idQueryIn);

    void markSelectedAsUnread(String idQueryIn);

    int save(Model model);

    int update(Model model);

    int delete(String idQueryIn);

    SysMessageReceive load(Integer id);

    List<SysMessageReceive> list(String idQueryIn);


}
