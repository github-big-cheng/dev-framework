package com.wisely.framework.handler.dictionary;

public interface ConverterDictionary {


    /**
     * 是否接收当前类型的值转换
     *
     * @param bizType
     * @return
     */
    boolean accept(String bizType);

    /**
     * 获取对应的值
     *
     * @param bizKey
     * @param valueKey
     * @return
     */
    default String loadValue(String bizKey, String valueKey) {
        // return bizKey when no result found
        return loadValue(bizKey, valueKey, bizKey);
    }

    /**
     * 获取对应的值，未找到返回默认值
     *
     * @param bizKey
     * @param valueKey
     * @param defaultValue
     * @return
     */
    String loadValue(String bizKey, String valueKey, String defaultValue);


}
