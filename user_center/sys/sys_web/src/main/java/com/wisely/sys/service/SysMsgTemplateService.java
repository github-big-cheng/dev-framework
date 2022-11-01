package com.wisely.sys.service;


import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.sys.entity.SysMessageTemplate;

import java.util.List;

public interface SysMsgTemplateService {
    /**
     * 新增消息模板
     */
    int save(Model model);

    /**
     * 删除消息模板
     */
    int delete(String idQueryIn);

    /**
     * 修改消息模板
     */
    int update(Model model);

    /**
     * id查询消息模板
     */
    SysMessageTemplate load(Integer id);


    /**
     * 消息模板业务类型列表
     */
    PageInfo<String> findByPage(PageVo pageVo);

    /**
     * 根据业务类型查询消息模板列表
     */
    List<SysMessageTemplate> findByBizType(String bizType);

}
