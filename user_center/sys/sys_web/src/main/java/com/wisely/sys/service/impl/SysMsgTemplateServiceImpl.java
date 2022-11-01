package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.sys.entity.SysMessageTemplate;
import com.wisely.sys.mapper.SysMessageTemplateMapper;
import com.wisely.sys.service.SysMsgTemplateService;

import javax.annotation.Resource;
import java.util.List;

public class SysMsgTemplateServiceImpl implements SysMsgTemplateService {

    @Resource
    SysMessageTemplateMapper sysMessageTemplateMapper;

    @Override
    public int save(Model model) {
        SysMessageTemplate record = this.modelToTemplate(model);
        SysMessageTemplate query = new SysMessageTemplate();
        query.setCode(record.getCode());
        List<SysMessageTemplate> queryRes = sysMessageTemplateMapper.selectListBySelective(query);
        if (ValidHelper.isNotEmpty(queryRes)) {
            return -1;
        }
        return sysMessageTemplateMapper.insertSelective(record);
    }

    @Override
    public int delete(String idQueryIn) {
        SysMessageTemplate record = new SysMessageTemplate();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        return 1;
    }

    @Override
    public int update(Model model) {
        SysMessageTemplate record = this.modelToTemplate(model);
        SysMessageTemplate query = new SysMessageTemplate();
        query.setCode(record.getCode());
        List<SysMessageTemplate> queryRes = sysMessageTemplateMapper.selectListBySelective(query);
        if (ValidHelper.isNotEmpty(queryRes)) {
            return -1;
        }
        return sysMessageTemplateMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SysMessageTemplate load(Integer id) {
        return sysMessageTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<String> findByPage(PageVo pageVo) {
        PageHelper.startPage(pageVo.getPageNo(), pageVo.getPageSize());
        return new PageInfo<String>(sysMessageTemplateMapper.selectDistinctBizType());
    }

    @Override
    public List<SysMessageTemplate> findByBizType(String bizType) {
        SysMessageTemplate query = new SysMessageTemplate();
        query.setBiztype(bizType);
        return sysMessageTemplateMapper.selectListBySelective(query);
    }

    private SysMessageTemplate modelToTemplate(Model model) {
        SysMessageTemplate record = (SysMessageTemplate) model.convertTo(SysMessageTemplate.class);
        record.setCreateBy(ValidHelper.isEmpty(model.getInt("create_by")) ? UserHelper.getUserId() : model.getInt("create_by"));
        record.setCreateTime(ValidHelper.isBlank(model.getString("create_time")) ? DateHelper.formatNow() : model.getString("create_time"));
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        return record;
    }
}
