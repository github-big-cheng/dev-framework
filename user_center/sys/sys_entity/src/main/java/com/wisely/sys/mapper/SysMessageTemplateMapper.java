package com.wisely.sys.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.sys.entity.SysMessageTemplate;

import java.util.List;

/**
 * 消息模板表(SysMessageTemplate)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-31 10:42:10
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {
    List<String> selectDistinctBizType();
}
