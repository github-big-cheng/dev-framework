package com.wisely.sys.service;

import com.wisely.framework.entity.Model;
import com.wisely.sys.entity.SysMessage;

import java.util.List;

public interface SysMsgService {
    int save(Model model);

    int update(Model model);

    int delete(String idQueryIn);
    List<SysMessage> selectIdQueryIn(String idQueryIn);

    List<SysMessage> selectIdQueryNotIn(String idQueryNotIn);

    List<SysMessage> selectBizTypeQueryIn(String bizTypeQueryIn);

    List<SysMessage> selectBizTypeQueryNotIn(String bizTypeQueryNotIn);

    List<SysMessage> selectMsgTypeQueryIn(String msgTypeQueryIn);

    List<SysMessage> selectMsgTypeQueryNotIn(String msgTypeQueryNotIn);

    List<SysMessage> selectTemplateIdQueryIn(String templateIdQueryIn);

    List<SysMessage> selectTemplateIdQueryNotIn(String templateIdQueryNotIn);

    List<SysMessage> selectSenderIdQueryIn(String senderIdQueryIn);

    List<SysMessage> selectSenderIdQueryNotIn(String senderIdQueryNotIn);
}
