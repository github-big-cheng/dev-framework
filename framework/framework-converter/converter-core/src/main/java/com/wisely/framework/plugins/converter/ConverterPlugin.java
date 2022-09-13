package com.wisely.framework.plugins.converter;

import com.wisely.framework.entity.Model;
import com.wisely.framework.handler.data.DataConverter;
import com.wisely.framework.handler.data.DataConverterImportSelector;
import com.wisely.framework.handler.operation.ConverterOperation;
import com.wisely.framework.handler.operation.DefaultConverterOperation;
import com.wisely.framework.handler.parser.ConverterDefineParser;
import com.wisely.framework.handler.parser.XmlConverterDefineParser;
import com.wisely.framework.handler.parser.YamlConverterDefineParser;
import com.wisely.framework.handler.regular.RegularTypeContext;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;


/**
 * 消息转换器
 * 请求数据校验
 * 返回数据过滤
 */
@ConditionalOnProperty(prefix = "plugins.converter", value = "enabled", havingValue = "true")
@Import(DataConverterImportSelector.class)
public class ConverterPlugin extends AbstractPlugin {


    @Override
    protected String getName() {
        return "ConverterPlugin";
    }


    @Bean
    @ConfigurationProperties(prefix = "plugins.converter")
    public ConverterProperties converterProperties() {
        return new ConverterProperties();
    }


    @Bean
    @ConditionalOnMissingBean(RegularTypeContext.class)
    public RegularTypeContext regularTypeContext() {
        return new RegularTypeContext(Model.builder());
    }


    /**
     * 报文定义解析器
     * 默认装载xml报文解析器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ConverterDefineParser.class)
    public ConverterDefineParser converterDefineParser(ConverterProperties converterProperties) {
        // xml
        if (StringHelper.equals(converterProperties.getDefineType(), "xml")) {
            return new XmlConverterDefineParser();
        }
        // 默认 yaml
        return new YamlConverterDefineParser();
    }


    /**
     * 默认报文转换器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ConverterOperation.class)
    public ConverterOperation converterOperation(List<DataConverter> dataConverts) {
        return new DefaultConverterOperation(dataConverts);
    }

}
