package com.wisely.ucenter.caches;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.mapper.UcenterOrgMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class OrgCache extends BaseEntityCache<UcenterOrg> implements UcenterConstants {

    @Resource
    UcenterOrgMapper ucenterOrgMapper;

    @Override
    public String getName() {
        return "OrgCache";
    }


    @Override
    protected String getKey() {
        return ORG_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(UcenterOrg item) {
        if (ValidHelper.isEmpty(item)) {
            return new byte[0];
        }
        return DataHelper.getString(item.getId()).getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, UcenterOrg item) {
        UcenterOrgVo orgVo =
                (UcenterOrgVo) Model.parseObject(item).convertTo(UcenterOrgVo.class);
        RedisHelper.hsetBytes(getKey(),
                model.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(orgVo)));
    }

    @Override
    protected List<UcenterOrg> loadData() {
        UcenterOrg query = new UcenterOrg();
        query.setIsDeleted(0);
        return ucenterOrgMapper.selectListBySelective(query);
    }
}
