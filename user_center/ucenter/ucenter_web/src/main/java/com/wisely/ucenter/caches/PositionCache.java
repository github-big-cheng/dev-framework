package com.wisely.ucenter.caches;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterPositionVo;
import com.wisely.ucenter.entity.UcenterPosition;
import com.wisely.ucenter.mapper.UcenterPositionMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
public class PositionCache extends BaseEntityCache<UcenterPosition> implements UcenterConstants {

    @Resource
    UcenterPositionMapper ucenterPositionMapper;

    @Override
    public String getName() {
        return "PositionCache";
    }

    @Override
    protected String getKey() {
        return POSITION_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(UcenterPosition item) {
        if (ValidHelper.isEmpty(item)) {
            return new byte[0];
        }
        return DataHelper.getString(item.getId()).getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, UcenterPosition item) {
        UcenterPositionVo positionVo =
                (UcenterPositionVo) Model.parseObject(item).convertTo(UcenterPositionVo.class);
        model.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(positionVo));
    }

    @Override
    protected List<UcenterPosition> loadData() {
        UcenterPosition query = new UcenterPosition();
        query.setIsDeleted(0);
        return ucenterPositionMapper.selectListBySelective(query);
    }
}
