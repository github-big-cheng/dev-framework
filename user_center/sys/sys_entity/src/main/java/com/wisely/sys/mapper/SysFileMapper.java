package com.wisely.sys.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.sys.entity.SysFile;

/**
 * 附件表(SysFile)表数据库访问层
 *
 * @author system
 * @since 2022-09-05 10:58:12
 */
public interface SysFileMapper extends BaseMapper<SysFile> {

    /**
     * 指定条件删除
     *      fileType
     *      sourceId
     *
     * @param sysFile
     * @return
     */
    int deleteBySelective(SysFile sysFile);

}
