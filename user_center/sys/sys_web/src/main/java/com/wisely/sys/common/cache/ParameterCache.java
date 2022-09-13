package com.wisely.sys.common.cache;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.exception.BusinessException;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.RedisHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.entity.SysParameter;
import com.wisely.sys.mapper.SysParameterMapper;
import com.wisely.sys.vo.SysParameterVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class ParameterCache extends BaseEntityCache<SysParameter> implements SysConstants {

    @Resource
    SysParameterMapper sysParameterMapper;


    @Override
    public String getName() {
        return "ParameterCache";
    }

    @Override
    protected String getKey() {
        return PARAMETER_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(SysParameter item) {
        return null;
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, SysParameter item) {
    }

    @Override
    protected List<SysParameter> loadData() {
        SysParameter query = new SysParameter();
        query.setIsDeleted(0);
        return sysParameterMapper.selectListBySelective(query);
    }


    @Override
    public void initCache() {
        this._init(this.loadData());
    }

    @Override
    public void syncCache(SysParameter... objs) {
        throw BusinessException.builder("UnSupport operation");
    }

    @Override
    public void invalidate(SysParameter... objs) {
        throw BusinessException.builder("UnSupport operation");
    }

    private void _init(List<SysParameter> dataList) {
        if (ValidHelper.isEmpty(dataList)) {
            log.warn("[{}] no cache data found!!!", this.getName());
            return;
        }

        // 以hash结构存储数据 SYS_PARAMETER_CACHE:1 k:v
        Model<Integer, Model<byte[], byte[]>> model = Model.builder();
        dataList.forEach(item -> {
            if (ValidHelper.isBlank(item.getKeies())) {
                return;
            }

            SysParameterVo cache = (SysParameterVo) Model.parseObject(item).convertTo(SysParameterVo.class);
            model.getModel(item.getOrgId(), true).set(item.getKeies().getBytes(), ProtoBufHelper.serializer(cache));
        });

        model.keySet().forEach(key -> {
            RedisHelper.hsetBytes(PARAMETER_CACHE_KEY + key, model.get(key));
        });
    }


    public void refreshByOrgId(Integer orgId) {
        // 非原子性，待优化

        // 按orgId删除
        RedisHelper.hdel(PARAMETER_CACHE_KEY + orgId);

        // 保存
        if (ValidHelper.isNull(orgId)) {
            return;
        }

        SysParameter query = new SysParameter();
        query.setOrgId(orgId);
        query.setIsDeleted(0);
        List<SysParameter> dataList = sysParameterMapper.selectListBySelective(query);
        this._init(dataList);
    }
}
