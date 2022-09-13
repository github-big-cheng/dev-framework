package com.wisely.ucenter.service;

import com.wisely.framework.entity.Model;
import com.wisely.ucenter.entity.UcenterOrg;

public interface UcenterOrgInfoService {

    Model<Integer, Model> orgInfoQuery(String orgIds);

    void save(UcenterOrg entity, Model info);
}
