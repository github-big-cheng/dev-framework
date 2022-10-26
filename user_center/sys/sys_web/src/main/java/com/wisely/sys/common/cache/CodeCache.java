package com.wisely.sys.common.cache;

import com.google.common.collect.Lists;
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
import java.util.function.BiConsumer;

/**
 * 系统代码
 */
@Slf4j
public class CodeCache extends BaseEntityCache<SysCode> implements SysConstants {

    @Resource
    SysCodeMapper sysCodeMapper;

    @Override
    public String getName() {
        return "CodeCache";
    }

    @Override
    protected List<CacheLoader<SysCode>> cacheLoaders() {

        List<CacheLoader<SysCode>> list = Lists.newArrayList();

        List<String> locales = sysCodeMapper.selectLocales();
        if (ValidHelper.isEmpty(locales)) {
            return list;
        }

        locales.forEach(locale -> list.add(new CodeByValueCacheLoader(locale)));
        return list;
    }

    @Override
    protected List<SysCode> loadData() {
        SysCode query = new SysCode();
        query.setIsDeleted(0);
        return sysCodeMapper.selectListBySelective(query);
    }

    /**
     * SysCodeVo.value -> SysCodeVo
     */
    class CodeByValueCacheLoader extends CacheLoader<SysCode> {

        public CodeByValueCacheLoader(String locale) {
            this.locale = locale;
            this.key = CODE_CACHE_KEY + locale;
        }

        String key;

        String locale;

        @Override
        public String key() {
            return key;
        }

        private boolean validation(SysCode item) {
            if (ValidHelper.isEmpty(item) || StringHelper.isBlank(item.getValue())) {
                return false;
            }

            if (!StringHelper.equals(item.getLocale(), this.locale)) {
                // 非当前处理记录行
                return false;
            }

            return true;
        }

        @Override
        public BiConsumer<Model<byte[], byte[]>, SysCode> addConsumer() {
            return (cacheModel, item) -> {
                if (!this.validation(item)) {
                    return;
                }

                SysCodeVo cache = (SysCodeVo) Model.parseObject(item).convertTo(SysCodeVo.class);
                cacheModel.set(item.getValue().getBytes(), ProtoBufHelper.serializer(cache));
            };
        }

        @Override
        public BiConsumer<List<byte[]>, SysCode> delConsumer() {
            return (list, item) -> {
                if (!this.validation(item)) {
                    return;
                }

                list.add(item.getValue().getBytes());
            };
        }
    }
}
