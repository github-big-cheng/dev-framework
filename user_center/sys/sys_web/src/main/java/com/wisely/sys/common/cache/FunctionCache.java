package com.wisely.sys.common.cache;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sso.client.SsoConstants;
import com.wisely.sso.client.entity.SsoFunction;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.entity.SysFunction;
import com.wisely.sys.mapper.SysFunctionMapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
public class FunctionCache extends BaseEntityCache<SysFunction> implements SysConstants, SsoConstants {

    @Resource
    SysFunctionMapper sysFunctionMapper;


    @Override
    public String getName() {
        return "FunctionCache";
    }


    @Override
    protected List<CacheLoader<SysFunction>> cacheLoaders() {
        return Lists.newArrayList(new FunctionByActionCacheLoader());
    }

    @Override
    protected List<SysFunction> loadData() {
        SysFunction query = new SysFunction();
        query.setIsDeleted(0);
        return sysFunctionMapper.selectListBySelective(query);
    }

    /**
     * SysFunctionVo.action -> SysFunctionVo
     */
    class FunctionByActionCacheLoader extends CacheLoader<SysFunction> {

        @Override
        public String key() {
            return FUNCTION_KEY;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, SysFunction> addConsumer() {
            return (cacheModel, item) -> {
                if (ValidHelper.isEmpty(item) || StringHelper.isBlank(item.getAction())) {
                    return;
                }

                // 单权限多接口支持
                List<String> actions = StringHelper.splitToList(item.getAction(), "|");

                SsoFunction functionVo =
                        (SsoFunction) Model.parseObject(item).convertTo(SsoFunction.class);
                actions.forEach(action -> cacheModel.set(action.getBytes(), ProtoBufHelper.serializer(functionVo)));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, SysFunction> delConsumer() {
            return (list, item) -> {
                if (ValidHelper.isEmpty(item) || StringHelper.isBlank(item.getAction())) {
                    return;
                }

                List<String> actions = StringHelper.splitToList(item.getAction(), "|");
                actions.forEach(action -> list.add(action.getBytes()));
            };
        }
    }
}
