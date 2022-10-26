package com.wisely.ucenter.caches;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterOrgVo;
import com.wisely.ucenter.entity.UcenterOrg;
import com.wisely.ucenter.service.UcenterOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 机构/部门缓存
 */
@Slf4j
public class OrgCache extends BaseEntityCache<Model> implements UcenterConstants {

    @Lazy
    @Resource
    UcenterOrgService ucenterOrgService;

    @Override
    public String getName() {
        return "OrgCache";
    }

    @Override
    protected List<CacheLoader<Model>> cacheLoaders() {
        return Lists.newArrayList(new OrgByIdCacheLoader());
    }

    @Override
    protected List<Model> loadData() {
        UcenterOrg query = new UcenterOrg();
        query.setIsDeleted(0);
        return ucenterOrgService.findList(query);
    }

    /**
     * UcenterOrgVo.id -> UcenterOrgVo
     */
    class OrgByIdCacheLoader extends CacheLoader<Model> {

        @Override
        public String key() {
            return ORG_CACHE_KEY;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, Model> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("id")) {
                    return;
                }

                UcenterOrgVo orgVo =
                        (UcenterOrgVo) item.convertTo(UcenterOrgVo.class);
                RedisHelper.hsetBytes(key(),
                        cacheModel.set(item.getString("id").getBytes(), ProtoBufHelper.serializer(orgVo)));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, Model> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || item.isBlank("id")) {
                    return;
                }

                list.add(item.getString("id").getBytes());
            };
        }
    }
}
