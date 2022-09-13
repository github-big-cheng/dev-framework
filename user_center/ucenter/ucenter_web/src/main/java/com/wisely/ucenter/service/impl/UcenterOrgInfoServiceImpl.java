package com.wisely.ucenter.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.entity.UcenterOrgInfo;
import com.wisely.ucenter.mapper.UcenterOrgInfoMapper;
import com.wisely.ucenter.service.UcenterOrgInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UcenterOrgInfoServiceImpl implements UcenterOrgInfoService {


    @Resource
    UcenterOrgInfoMapper ucenterOrgInfoMapper;


    /**
     * 人员信息查询
     *
     * @param orgIds
     * @return
     */
    @Override
    public Model<Integer, Model> orgInfoQuery(String orgIds) {

        Model<Integer, Model> result = Model.builder();

        UcenterOrgInfo query = new UcenterOrgInfo();
        query.setOrgIdQueryIn(orgIds);
        query.setIsDeleted(0);
        List<UcenterOrgInfo> list = ucenterOrgInfoMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(list)) {
            return result;
        }

        list.forEach(info ->
                result.getModel(info.getOrgId(), true)
                        .set(info.getFieldKey(), info.getFieldValue()));
        return result;
    }


    /**
     * 保存
     *
     * @param entity
     * @param info
     */
    @Transactional
    @Override
    public void save(UcenterOrg entity, Model info) {

        // 先删除
        ucenterOrgInfoMapper.deleteByPersonId(entity.getId());

        if (ValidHelper.isEmpty(info)) {
            return;
        }

        Integer createBy = UserHelper.getUserId();
        String now = DateHelper.formatNow();

        info.keySet().forEach(key -> {
            UcenterOrgInfo record = new UcenterOrgInfo();
            record.setOrgId(entity.getId());
            record.setFieldKey(DataHelper.getString(key));
            record.setFieldValue(info.getString(key));
            record.setIsDeleted(0);
            record.setCreateBy(createBy);
            record.setCreateTime(now);
            ucenterOrgInfoMapper.insertSelective(record);
        });
    }

}
