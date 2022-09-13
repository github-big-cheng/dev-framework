package com.wisely.sys.service;


import com.wisely.sys.entity.SysFile;
import com.wisely.framework.entity.Model;

import java.util.List;

/**
 * 资源文件(SysFile)表服务接口
 *
 * @author ruijie.hu
 * @since 2021-06-011 17:56:46
 */
public interface SysFileService {


    /**
     * 动态查询
     *
     * @param sysFile
     * @return
     */
    List<SysFile> selectListBySelective(SysFile sysFile);

    /**
     * 添加
     *
     * @return
     */
    int save(Model input);


    /**
     * 根据sourceType和sourceId删除对应的附件
     *
     * @param sysFile
     * @return
     */
    int delete(SysFile sysFile);
}
