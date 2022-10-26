package com.wisely.ucenter.caches;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterRoleVo;
import com.wisely.ucenter.entity.UcenterRole;
import com.wisely.ucenter.mapper.UcenterRoleMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 角色缓存
 */
@Slf4j
public class RoleCache extends BaseEntityCache<UcenterRole> implements UcenterConstants {

    @Resource
    UcenterRoleMapper ucenterRoleMapper;

    @Override
    public String getName() {
        return "RoleCache";
    }

    @Override
    protected List<CacheLoader<UcenterRole>> cacheLoaders() {
        return Lists.newArrayList(new RoleByIdCacheLoader());
    }

    @Override
    protected List<UcenterRole> loadData() {
        UcenterRole query = new UcenterRole();
        query.setIsDeleted(0);
        return ucenterRoleMapper.selectListBySelective(query);
    }

    /**
     * UcenterRoleVo.id -> UcenterRoleVo
     */
    class RoleByIdCacheLoader extends CacheLoader<UcenterRole> {

        @Override
        public String key() {
            return ROLE_CACHE_KEY;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, UcenterRole> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                UcenterRoleVo roleVo = (UcenterRoleVo) Model.parseObject(item).convertTo(UcenterRoleVo.class);
                cacheModel.set(DataHelper.getString(roleVo.getId()).getBytes(), ProtoBufHelper.serializer(roleVo));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, UcenterRole> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || ValidHelper.isNull(item.getId())) {
                    return;
                }

                list.add(DataHelper.getString(item.getId()).getBytes());
            };
        }
    }
}
