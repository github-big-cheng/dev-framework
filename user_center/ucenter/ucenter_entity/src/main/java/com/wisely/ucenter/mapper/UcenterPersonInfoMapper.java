package com.wisely.ucenter.mapper;

import com.wisely.framework.entity.BaseMapper;
import com.wisely.ucenter.entity.UcenterPersonInfo;

/**
 * 人员信息详情表(UcenterPersonInfo)表数据库访问层
 *
 * @author system
 * @since 2022-09-07 11:41:55
 */
public interface UcenterPersonInfoMapper extends BaseMapper<UcenterPersonInfo> {

    int deleteByPersonId(Integer personId);

}
