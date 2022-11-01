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
    public int delete(String idQueryIn) {
        SysMessage record = new SysMessage();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        return sysMessageMapper.updateBySelective(record);
    }

    @Override
    public List<SysMessage> selectList(SysMessage query) {
        return sysMessageMapper.selectListBySelective(query);
    }


    public SysMessage modelToMessage(Model model) {
        SysMessage record = (SysMessage)model.convertTo(SysMessage.class);
        return record;
    }
}
