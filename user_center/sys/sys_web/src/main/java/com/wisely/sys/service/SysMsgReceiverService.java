package com.wisely.sys.service;

import com.wisely.sys.entity.SysMessageReceive;

import java.util.List;

public interface SysMsgReceiverService {

    void markSelectedAsRead(String idQueryIn);

    void markSelectedAsUnread(String idQueryIn);

    void markAllAsRead();

    void markAllAsUnread();

    int delete(String idQueryIn);

    List<SysMessageReceive> list(String idQueryIn);


}
