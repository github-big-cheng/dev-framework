package com.wisely.sys.service;


import com.github.pagehelper.PageInfo;
import com.wisely.framework.entity.Model;
import com.wisely.framework.entity.PageVo;
import com.wisely.sys.entity.SysParameter;

import java.util.List;

/**
 * 自定义页面配置表(TSysColumnConfig)表服务接口
 *
 * @author ruijie.hu
 * @since 2021-06-03 17:56:46
 */
public interface SysParameterService {

    /**
     * 客户自定义页面配置查询
     * 未配置则返回系统默认配置
     *
     * @param query
     * @return
     */
    List<SysParameter> list(SysParameter query);

    /**
     * 分业查询
     *
     * @param sysParameter
     * @param pageVo
     * @return
     */
    PageInfo findPage(SysParameter sysParameter, PageVo pageVo);

    /**
     * 通过ORGID和KEIES查询
     *
     * @return
     */
    Model load();

    /**
     * 保存
     */
    int save();

    /**
     * 刷新缓存
     */
    void refresh(Integer orgId);
}
