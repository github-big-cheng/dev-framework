package com.wisely.framework.plugins.converter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConverterProperties {

    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 报文定义根路径
     */
    private String defineRootPath = "classpath*:/converter";
    /**
     * 是否开启接口调试工具
     * #Deprecated
     * 引入converter-static包 自动装载
     */
    @Deprecated
    private boolean useApi;

    /**
     * 报文定义类型
     * - yml
     * - xml
     */
    private String defineType = "xml";
}
