package com.wisely.ucenter.caches;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.*;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterUserVo;
import com.wisely.ucenter.entity.UcenterUser;
import com.wisely.ucenter.mapper.UcenterUserMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class UserCache extends BaseEntityCache<UcenterUser> implements UcenterConstants {

    @Resource
    UcenterUserMapper ucenterUserMapper;

    @Override
    public String getName() {
        return "UserCache";
    }

    @Override
    protected String getKey() {
        return USER_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(UcenterUser item) {
        if (ValidHelper.isEmpty(item)) {
            return new byte[0];
        }
        return DataHelper.getString(item.getId()).getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, UcenterUser item) {
        UcenterUserVo userVo =
                (UcenterUserVo) Model.parseObject(item).convertTo(UcenterUserVo.class);
        model.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(userVo));
    }

    @Override
    protected List<UcenterUser> loadData() {
        UcenterUser query = new UcenterUser();
        query.setIsDeleted(0);
        return ucenterUserMapper.selectListBySelective(query);
    }
}
