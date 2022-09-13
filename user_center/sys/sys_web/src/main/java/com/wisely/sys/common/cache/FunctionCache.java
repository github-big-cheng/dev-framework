package com.wisely.sys.common.cache;

import com.google.common.collect.Lists;
import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
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
import java.util.stream.Stream;

@Slf4j
public class FunctionCache extends BaseEntityCache<SysFunction> implements SysConstants, SsoConstants {

    @Resource
    SysFunctionMapper sysFunctionMapper;


    @Override
    public String getName() {
        return "FunctionCache";
    }


    @Override
    protected String getKey() {
        return FUNCTION_KEY;
    }

    @Override
    protected byte[] loadField(SysFunction function) {
        return new byte[0];
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, SysFunction item) {
        // 单权限多接口支持
        List<String> actions = StringHelper.splitToList(item.getAction(), "|");

        SsoFunction functionVo =
                (SsoFunction) Model.parseObject(item).convertTo(SsoFunction.class);
        actions.forEach(action -> model.set(action.getBytes(), ProtoBufHelper.serializer(functionVo)));
    }

    @Override
    protected List<SysFunction> loadData() {
        SysFunction query = new SysFunction();
        query.setIsDeleted(0);
        return sysFunctionMapper.selectListBySelective(query);
    }

    public void invalidate(SysFunction... objs) {
        if (ValidHelper.isEmpty(objs)) {
            return;
        }

        List<byte[]> list = Lists.newArrayList();
        Stream.of(objs).forEach(item -> {
            List<String> actions = StringHelper.splitToList(item.getAction(), "|");
            actions.forEach(action -> list.add(action.getBytes()));
        });

        byte[][] fields = new byte[list.size()][];
        Stream.iterate(0, (i) -> i + 1)
                .limit(fields.length)
                .forEach((inx) -> fields[inx] = list.get(inx));
        RedisHelper.hdel(this.getKey().getBytes(), fields);
    }
}
