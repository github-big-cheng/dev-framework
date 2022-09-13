package com.wisely.sys.common.cache;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.sys.common.SysConstants;
import com.wisely.sys.entity.SysCode;
import com.wisely.sys.mapper.SysCodeMapper;
import com.wisely.sys.vo.SysCodeVo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class CodeCache extends BaseEntityCache<SysCode> implements SysConstants {

    @Resource
    SysCodeMapper sysCodeMapper;


    @Override
    public String getName() {
        return "CodeCache";
    }

    @Override
    protected String getKey() {
        return CODE_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(SysCode item) {
        if (ValidHelper.isEmpty(item) || StringHelper.isBlank(item.getValue())) {
            return new byte[0];
        }
        return item.getValue().getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, SysCode item) {
        SysCodeVo codeVo =
                (SysCodeVo) Model.parseObject(item).convertTo(SysCodeVo.class);
        model.set(item.getValue().getBytes(), ProtoBufHelper.serializer(codeVo));
    }

    @Override
    protected List<SysCode> loadData() {
        SysCode query = new SysCode();
        query.setIsDeleted(0);
        return sysCodeMapper.selectListBySelective(query);
    }
}
