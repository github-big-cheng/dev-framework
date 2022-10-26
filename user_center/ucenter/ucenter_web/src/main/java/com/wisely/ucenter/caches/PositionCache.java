package com.wisely.ucenter.caches;

import com.google.common.collect.Lists;
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
import java.util.function.BiConsumer;

/**
 * 职位缓存
 */
@Slf4j
public class PositionCache extends BaseEntityCache<UcenterPosition> implements UcenterConstants {

    @Resource
    UcenterPositionMapper ucenterPositionMapper;

    @Override
    public String getName() {
        return "PositionCache";
    }

    @Override
    protected List<CacheLoader<UcenterPosition>> cacheLoaders() {
        return Lists.newArrayList(new PositionByIdCacheLoader());
    }

    @Override
    protected List<UcenterPosition> loadData() {
        UcenterPosition query = new UcenterPosition();
        query.setIsDeleted(0);
        return ucenterPositionMapper.selectListBySelective(query);
    }

    /**
     * UcenterPositionVo.id -> UcenterPositionVo
     */
    class PositionByIdCacheLoader extends CacheLoader<UcenterPosition> {

        @Override
        public String key() {
            return POSITION_CACHE_KEY;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, UcenterPosition> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isNull(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                UcenterPositionVo positionVo =
                        (UcenterPositionVo) Model.parseObject(item).convertTo(UcenterPositionVo.class);
                cacheModel.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(positionVo));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, UcenterPosition> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isNull(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                list.add(DataHelper.getString(item.getId()).getBytes());
            };
        }
    }
}
