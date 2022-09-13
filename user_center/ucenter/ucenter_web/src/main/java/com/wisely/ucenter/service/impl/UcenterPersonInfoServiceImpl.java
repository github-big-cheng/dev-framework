package com.wisely.ucenter.service.impl;

import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.DateHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.helper.UserHelper;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.entity.UcenterPersonInfo;
import com.wisely.ucenter.mapper.UcenterPersonInfoMapper;
import com.wisely.ucenter.service.UcenterPersonInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UcenterPersonInfoServiceImpl implements UcenterPersonInfoService {


    @Resource
    UcenterPersonInfoMapper ucenterPersonInfoMapper;


    /**
     * 人员信息查询
     *
     * @param personIds
     * @return
     */
    @Override
    public Model<Integer, Model> personInfoQuery(String personIds) {

        Model<Integer, Model> result = Model.builder();

        UcenterPersonInfo query = new UcenterPersonInfo();
        query.setPersonIdQueryIn(personIds);
        query.setIsDeleted(0);
        List<UcenterPersonInfo> list = ucenterPersonInfoMapper.selectListBySelective(query);
        if (ValidHelper.isEmpty(list)) {
            return result;
        }

        list.forEach(info ->
                result.getModel(info.getPersonId(), true)
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
    public void save(UcenterPerson entity, Model info) {

        // 先删除
        ucenterPersonInfoMapper.deleteByPersonId(entity.getId());

        if (ValidHelper.isEmpty(info)) {
            return;
        }

        Integer createBy = UserHelper.getUserId();
        String now = DateHelper.formatNow();

        info.keySet().forEach(key -> {
            UcenterPersonInfo record = new UcenterPersonInfo();
            record.setPersonId(entity.getId());
            record.setFieldKey(DataHelper.getString(key));
            record.setFieldValue(info.getString(key));
            record.setIsDeleted(0);
            record.setCreateBy(createBy);
            record.setCreateTime(now);
            ucenterPersonInfoMapper.insertSelective(record);
        });
    }

}
