package com.wisely.sys.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DateHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.entity.SysMessageReceive;
import com.wisely.sys.mapper.SysMessageReceiveMapper;
import com.wisely.sys.service.SysMsgReceiverService;

import javax.annotation.Resource;
import java.util.List;

public class SysMsgReceiverServiceImpl implements SysMsgReceiverService {

    @Resource
    SysMessageReceiveMapper sysMessageReceiveMapper;

    @Override
    public void markSelectedAsRead(String idQueryIn) {
        SysMessageReceive record = new SysMessageReceive();
        record.setReceiveStatus(1);
        record.setIdQueryIn(idQueryIn);
        sysMessageReceiveMapper.updateBySelective(record);
    }

    @Override
    public void markSelectedAsUnread(String idQueryIn) {
        SysMessageReceive record = new SysMessageReceive();
        record.setReceiveStatus(0);
        record.setIdQueryIn(idQueryIn);
        sysMessageReceiveMapper.updateBySelective(record);
    }

    @Override
    public void markAllAsRead() {
        SysMessageReceive record = new SysMessageReceive();
        record.setReceiveStatus(1);
        sysMessageReceiveMapper.batchUpdate(record);
    }

    @Override
    public void markAllAsUnread() {
        SysMessageReceive record = new SysMessageReceive();
        record.setReceiveStatus(0);
        sysMessageReceiveMapper.batchUpdate(record);
    }

    @Override
    public int delete(String idQueryIn) {
        SysMessageReceive query = new SysMessageReceive();
        query.setIdQueryIn(idQueryIn);
        query.setIsDeleted(1);
        query.setUpdateBy(UserHelper.getUserId());
        query.setUpdateTime(DateHelper.formatNow());
        return sysMessageReceiveMapper.updateBySelective(query);
    }



    @Override
    public List<SysMessageReceive> list(String idQueryIn) {
        SysMessageReceive query = new SysMessageReceive();
        query.setIdQueryIn(idQueryIn);
        return sysMessageReceiveMapper.selectListBySelective(query);
    }

    public SysMessageReceive modelToMsgReceive(Model model) {
        SysMessageReceive record = (SysMessageReceive)model.convertTo(SysMessageReceive.class);
        return record;
    }
}
