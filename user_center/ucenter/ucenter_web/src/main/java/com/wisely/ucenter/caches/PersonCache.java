package com.wisely.ucenter.caches;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.DataHelper;
import com.wisely.framework.helper.ProtoBufHelper;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.client.common.UcenterConstants;
import com.wisely.ucenter.client.vo.UcenterPersonVo;
import com.wisely.ucenter.entity.UcenterPerson;
import com.wisely.ucenter.service.UcenterPersonService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class PersonCache extends BaseEntityCache<UcenterPerson> implements UcenterConstants {

    @Resource
    UcenterPersonService ucenterPersonService;

    @Override
    public String getName() {
        return "PersonCache";
    }

    @Override
    protected String getKey() {
        return PERSON_CACHE_KEY;
    }

    @Override
    protected byte[] loadField(UcenterPerson item) {
        if (ValidHelper.isEmpty(item)) {
            return new byte[0];
        }
        return DataHelper.getString(item.getId()).getBytes();
    }

    @Override
    protected void setItem(Model<byte[], byte[]> model, UcenterPerson item) {
        UcenterPersonVo personVo =
                (UcenterPersonVo) Model.parseObject(item).convertTo(UcenterPersonVo.class);
        model.set(DataHelper.getString(item.getId()).getBytes(), ProtoBufHelper.serializer(personVo));
    }

    @Override
    protected List<UcenterPerson> loadData() {
        UcenterPerson query = new UcenterPerson();
        query.setIsDeleted(0);
        return ucenterPersonService.findList(query);
    }
}
