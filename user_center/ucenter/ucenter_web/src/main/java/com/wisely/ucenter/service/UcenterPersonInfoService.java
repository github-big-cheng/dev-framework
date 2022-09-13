package com.wisely.ucenter.service;

import com.wisely.framework.entity.Model;
import com.wisely.ucenter.entity.UcenterPerson;

public interface UcenterPersonInfoService {

    Model<Integer, Model> personInfoQuery(String personIds);

    void save(UcenterPerson entity, Model info);
}
