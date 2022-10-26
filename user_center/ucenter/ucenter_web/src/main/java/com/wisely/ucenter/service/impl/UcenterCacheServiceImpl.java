package com.wisely.ucenter.service.impl;

import com.wisely.framework.entity.BaseEntityCache;
import com.wisely.framework.entity.Model;
import com.wisely.framework.helper.ValidHelper;
import com.wisely.ucenter.service.UcenterCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

@Slf4j
@Service
public class UcenterCacheServiceImpl implements UcenterCacheService {


    private Model<String, BaseEntityCache> cacheModel = Model.builder();

    @Autowired
    public void setCacheModel(List<BaseEntityCache> cacheList) {
        if (ValidHelper.isEmpty(cacheList)) {
            return;
        }
        cacheList.forEach(cache -> this.cacheModel.set(cache.getName(), cache));
        log.info("cacheModel: {}", this.cacheModel);
    }

    @Override
    public void refreshByType(String type) {

        StopWatch stopWatch = new StopWatch();

        if (ValidHelper.isEmpty(type)) {
            for (String key : cacheModel.keySet()) {
                stopWatch.start(key);
                cacheModel.get(key).clearCache();
                cacheModel.get(key).initCache();
                stopWatch.stop();
            }
        } else {
            BaseEntityCache cacheObj = cacheModel.get(type);
            if (ValidHelper.isNotEmpty(cacheObj)) {
                stopWatch.start(type);
                cacheObj.clearCache();
                cacheObj.initCache();
                stopWatch.stop();
            }
        }

        log.info("cache refresh completed: \r\n{}", stopWatch.prettyPrint());
    }
}
