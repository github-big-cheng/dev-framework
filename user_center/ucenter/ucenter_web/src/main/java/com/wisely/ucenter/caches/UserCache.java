package com.wisely.ucenter.caches;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterUserVo;
import com.wisely.ucenter.entity.UcenterUser;
import com.wisely.ucenter.mapper.UcenterUserMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 用户缓存
 */
@Slf4j
public class UserCache extends BaseEntityCache<UcenterUser> implements UcenterConstants {

    @Resource
    UcenterUserMapper ucenterUserMapper;

    @Override
    public String getName() {
        return "UserCache";
    }

    @Override
    protected List<CacheLoader<UcenterUser>> cacheLoaders() {
        return Lists.newArrayList(new UserByIdCacheLoader());
    }

    @Override
    protected List<UcenterUser> loadData() {
        UcenterUser query = new UcenterUser();
        query.setIsDeleted(0);
        return ucenterUserMapper.selectListBySelective(query);
    }


    /**
     * UcenterUserVo.id -> UcenterUserVo
     */
    class UserByIdCacheLoader extends CacheLoader<UcenterUser> {

        @Override
        public String key() {
            return USER_CACHE_KEY;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, UcenterUser> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                UcenterUserVo userVo =
                        (UcenterUserVo) Model.parseObject(item).convertTo(UcenterUserVo.class);
                cacheModel.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(userVo));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, UcenterUser> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                list.add(DataHelper.getString(item.getId()).getBytes());
            };
        }
    }
}
