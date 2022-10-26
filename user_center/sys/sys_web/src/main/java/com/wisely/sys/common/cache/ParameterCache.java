package com.wisely.sys.common.cache;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.entity.SysParameter;
import com.wisely.sys.mapper.SysParameterMapper;
import com.wisely.sys.vo.SysParameterVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
public class ParameterCache extends BaseEntityCache<SysParameter> implements SysConstants {

    @Resource
    SysParameterMapper sysParameterMapper;


    @Override
    public String getName() {
        return "ParameterCache";
    }

    @Override
    protected List<CacheLoader<SysParameter>> cacheLoaders() {

        List<CacheLoader<SysParameter>> list = Lists.newArrayList();

        List<Integer> orgIdList = sysParameterMapper.selectOrgIds();
        if (ValidHelper.isEmpty(orgIdList)) {
            return list;
        }

        orgIdList.forEach(orgId -> list.add(new ParameterByOrgIdAndKeys(orgId)));
        return list;
    }

    @Override
    protected List<SysParameter> loadData() {
        SysParameter query = new SysParameter();
        query.setIsDeleted(0);
        return sysParameterMapper.selectListBySelective(query);
    }

    /**
     * 按机构更新缓存
     *
     * @param orgId
     */
    public void refreshByOrgId(Integer orgId) {
        // 非原子性，待优化

        // 按orgId删除
        RedisHelper.del(PARAMETER_CACHE_KEY + orgId);

        // 保存
        if (ValidHelper.isNull(orgId)) {
            return;
        }

        SysParameter query = new SysParameter();
        query.setOrgId(orgId);
        query.setIsDeleted(0);
        List<SysParameter> dataList = sysParameterMapper.selectListBySelective(query);
        this.syncCache(dataList.toArray(new SysParameter[0]));
    }


    class ParameterByOrgIdAndKeys extends CacheLoader<SysParameter> {

        public ParameterByOrgIdAndKeys(Integer orgId) {
            this.orgId = orgId;
        }

        private Integer orgId;

        @Override
        public String key() {
            return PARAMETER_CACHE_KEY + orgId;
        }

        private boolean validation(SysParameter item) {

            if (ValidHelper.isEmpty(item) || ValidHelper.isNull(item.getId())) {
                return false;
            }

            if (StringHelper.isBlank(item.getKeies())) {
                return false;
            }

            if (!ValidHelper.isEquals(item.getId(), this.orgId)) {
                return false;
            }

            return true;
        }


        @Override
        public BiConsumer<Model<byte[], byte[]>, SysParameter> addConsumer() {
            return (cacheModel, item) -> {
                if (!this.validation(item)) {
                    return;
                }

                SysParameterVo cache = (SysParameterVo) Model.parseObject(item).convertTo(SysParameterVo.class);
                cacheModel.set(item.getKeies().getBytes(), ProtoBufHelper.serializer(cache));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, SysParameter> delConsumer() {
            return (list, item) -> {
                if (this.validation(item)) {
                    return;
                }

                list.add(item.getKeies().getBytes());
            };
        }
    }
}
