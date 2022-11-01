package com.wisely.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.framework.helper.AssertHelper;
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
    public int save(SysMessageTemplate record) {
        SysMessageTemplate query = new SysMessageTemplate();
        query.setCode(record.getCode());
        query.setIsDeleted(0);
        List<SysMessageTemplate> queryRes = sysMessageTemplateMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(record.getId())) {
            // 新增前声明无相同code记录
            AssertHelper.EX_BUSINESS.isEmpty(queryRes, "sys_msg_template.repeat_code_found.{0}", record.getCode());
            record.setCreateBy(UserHelper.getUserId());
            record.setCreateTime(DateHelper.formatNow());
            record.setIsDeleted(0);
            sysMessageTemplateMapper.insertSelective(record);
        } else {
            // 更新前声明唯一记录id与更新数据id相同
            if (ValidHelper.isNotEmpty(queryRes)) {
                queryRes.forEach(template ->
                                AssertHelper.EX_BUSINESS.isEquals(
                                        record.getId(),
                                        template.getId(),
                                        "sys_msg_template.inconsistent_record_found.{0}", template.getId()
                                ));
            }
            record.setUpdateBy(UserHelper.getUserId());
            record.setUpdateTime(DateHelper.formatNow());
            sysMessageTemplateMapper.updateByPrimaryKeySelective(record);
        }
        return record.getId();
    }

    @Override
    public int delete(String idQueryIn) {
        SysMessageTemplate record = new SysMessageTemplate();
        record.setIdQueryIn(idQueryIn);
        record.setIsDeleted(1);
        record.setUpdateBy(UserHelper.getUserId());
        record.setUpdateTime(DateHelper.formatNow());
        return sysMessageTemplateMapper.updateBySelective(record);
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

    public SysMessageTemplate modelToTemplate(Model model) {
        SysMessageTemplate record = (SysMessageTemplate) model.convertTo(SysMessageTemplate.class);
        return record;
    }
}
