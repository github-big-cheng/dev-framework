package com.wisely.ucenter.caches;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.entity.UcenterRole;
import com.wisely.ucenter.mapper.UcenterRoleMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class RoleCache extends BaseEntityCache<UcenterRole> implements UcenterConstants {

    @Resource
    UcenterRoleMapper ucenterRoleMapper;

    @Override
    public String getName() {
        return "RoleCache";
    }

    @Override
    protected String getKey() {
        return ROLE_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(UcenterRole item) {
        if (ValidHelper.isEmpty(item)) {
            return new byte[0];
        }
        return DataHelper.getString(item.getId()).getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, UcenterRole item) {
        model.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(item));
    }

    @Override
    protected List<UcenterRole> loadData() {
        UcenterRole query = new UcenterRole();
        query.setIsDeleted(0);
        return ucenterRoleMapper.selectListBySelective(query);
    }
}
