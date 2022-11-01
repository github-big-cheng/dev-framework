package com.wisely.sys.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.entity.SysMessage;
import com.wisely.sys.mapper.SysMessageMapper;
import com.wisely.sys.service.SysMsgService;

import javax.annotation.Resource;
import java.util.List;

public class SysMsgServiceImpl implements SysMsgService {

    @Resource
    SysMessageMapper sysMessageMapper;

    @Override
    public int save(Model model) {
        SysMessage record = this.modelToMessage(model);

        return sysMessageMapper.insertSelective(record);
    }

    @Override
    public int update(Model model) {
        SysMessage record = this.modelToMessage(model);

        return sysMessageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int delete(String idQueryIn) {
        SysMessage record = new SysMessage();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        return sysMessageMapper.batchDelete(record);
    }

    @Override
    public List<SysMessage> selectIdQueryIn(String idQueryIn) {
        SysMessage query = new SysMessage();
        query.setIdQueryIn(idQueryIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectIdQueryNotIn(String idQueryNotIn) {
        SysMessage query = new SysMessage();
        query.setIdQueryNotIn(idQueryNotIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectBizTypeQueryIn(String bizTypeQueryIn) {
        SysMessage query = new SysMessage();
        query.setBizTypeQueryIn(bizTypeQueryIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectBizTypeQueryNotIn(String bizTypeQueryNotIn) {
        SysMessage query = new SysMessage();
        query.setBizTypeQueryNotIn(bizTypeQueryNotIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectMsgTypeQueryIn(String msgTypeQueryIn) {
        SysMessage query = new SysMessage();
        query.setMsgTypeQueryIn(msgTypeQueryIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectMsgTypeQueryNotIn(String msgTypeQueryNotIn) {
        SysMessage query = new SysMessage();
        query.setMsgTypeQueryNotIn(msgTypeQueryNotIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectTemplateIdQueryIn(String templateIdQueryIn) {
        SysMessage query = new SysMessage();
        query.setTemplateIdQueryIn(templateIdQueryIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectTemplateIdQueryNotIn(String templateIdQueryNotIn) {
        SysMessage query = new SysMessage();
        query.setTemplateIdQueryNotIn(templateIdQueryNotIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectSenderIdQueryIn(String senderIdQueryIn) {
        SysMessage query = new SysMessage();
        query.setSenderIdQueryIn(senderIdQueryIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    @Override
    public List<SysMessage> selectSenderIdQueryNotIn(String senderIdQueryNotIn) {
        SysMessage query = new SysMessage();
        query.setSenderIdQueryNotIn(senderIdQueryNotIn);
        return sysMessageMapper.selectListBySelective(query);
    }

    private SysMessage modelToMessage(Model model) {
        SysMessage record = (SysMessage)model.convertTo(SysMessage.class);
        record.setCreateBy(ValidHelper.isEmpty(model.getInt("create_by")) ? UserHelper.getUserId() : model.getInt("create_by"));
        record.setCreateTime(ValidHelper.isBlank(model.getString("create_time")) ? DateHelper.formatNow() : model.getString("create_time"));
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());

        return record;
    }
}
