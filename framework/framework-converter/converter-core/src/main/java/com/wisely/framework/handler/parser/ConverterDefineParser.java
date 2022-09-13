package com.wisely.framework.handler.parser;

import com.wisely.framework.handler.entity.ConverterEntity;
import com.wisely.framework.helper.SpringHelper;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.converter.ConverterProperties;

import java.io.File;

public interface ConverterDefineParser {

    /**
     * 获取文件路径
     *
     * @param key
     * @return
     */
    default String loadPath(String key) {
        ConverterProperties converterProperties = SpringHelper.getBean(ConverterProperties.class);
        String defineRootPath = converterProperties.getDefineRootPath();
        String separator = StringHelper.startsWith(defineRootPath, "classpath") ? "/" : File.separator;
        return converterProperties.getDefineRootPath() + separator + key;
    }

    /**
     * 报文定义转换
     *
     * @param key
     * @return
     */
    ConverterEntity loadConfig(String key);


    /**
     * 刷新指定Key
     *
     * @param key
     * @return
     */
    void refreshConfig(String key) throws Exception;
}
