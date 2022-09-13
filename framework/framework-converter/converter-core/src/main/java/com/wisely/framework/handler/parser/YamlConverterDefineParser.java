package com.wisely.framework.handler.parser;

import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.helper.ResourceHelper;
import com.wisely.framework.helper.ValidHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
public class YamlConverterDefineParser implements ConverterDefineParser {


    private static ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


    private LoadingCache<String, Optional<ConverterEntity>> CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(512) // 最大存储数量
                    .expireAfterAccess(5, TimeUnit.MINUTES) // 设置过期时间 5分钟
                    .build(new CacheLoader<String, Optional<ConverterEntity>>() {
                        @Override
                        public Optional<ConverterEntity> load(String key) throws Exception {
                            InputStream reader = ResourceHelper.getInputStream(loadPath(key) + ".yml");
                            ConverterEntity entity = objectMapper.readValue(reader, ConverterEntity.class);
                            return Optional.of(entity);
                        }
                    });


    @Override
    public ConverterEntity loadConfig(String key) {
        try {
            return CACHE.get(key).orNull();
        } catch (Exception e) {
            log.error("loadConfig error key=>{} : {}", key, e);
        }
        return null;
    }

    @Override
    public void refreshConfig(String key) {

        if (ValidHelper.isBlank(key)) {
            CACHE.cleanUp();
            return;
        }

        Optional<ConverterEntity> ifPresent = CACHE.getIfPresent(key);
        if (ValidHelper.isNotEmpty(ifPresent) && ifPresent.isPresent()) {
            CACHE.refresh(key);
        }
    }
}
