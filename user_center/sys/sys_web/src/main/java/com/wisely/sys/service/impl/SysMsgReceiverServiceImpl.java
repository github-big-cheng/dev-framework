package com.wisely.sys.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
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
    public void markAsRead(Integer id) {
        SysMessageReceive record = new SysMessageReceive();
        record.setId(id);
        record.setReceiveStatus(1);
        sysMessageReceiveMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void markAsUnread(Integer id) {
        SysMessageReceive record = new SysMessageReceive();
        record.setId(id);
        record.setReceiveStatus(0);
        sysMessageReceiveMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public void markSelectedAsRead(String idQueryIn) {
        sysMessageReceiveMapper.batchUpdateAsRead(idQueryIn);
    }

    @Override
    public void markSelectedAsUnread(String idQueryIn) {
        sysMessageReceiveMapper.batchUpdateAsUnread(idQueryIn);
    }

    @Override
    public int save(Model model) {
        SysMessageReceive record = this.modelToMsgReceive(model);
        return sysMessageReceiveMapper.insertSelective(record);
    }

    @Override
    public int update(Model model) {
        SysMessageReceive record = this.modelToMsgReceive(model);
        return sysMessageReceiveMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int delete(String idQueryIn) {
        return sysMessageReceiveMapper.batchDelete(idQueryIn);
    }

    @Override
    public SysMessageReceive load(Integer id) {
        return sysMessageReceiveMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysMessageReceive> list(String idQueryIn) {
        SysMessageReceive query = new SysMessageReceive();
        query.setIdQueryIn(idQueryIn);
        return sysMessageReceiveMapper.selectListBySelective(query);
    }

    private SysMessageReceive modelToMsgReceive(Model model) {
        SysMessageReceive record = (SysMessageReceive)model.convertTo(SysMessageReceive.class);
        record.setCreateBy(ValidHelper.isEmpty(model.getInt("create_by")) ? UserHelper.getUserId() : model.getInt("create_by"));
        record.setCreateTime(ValidHelper.isBlank(model.getString("create_time")) ? DateHelper.formatNow() : model.getString("create_time"));
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        return record;
    }
}
