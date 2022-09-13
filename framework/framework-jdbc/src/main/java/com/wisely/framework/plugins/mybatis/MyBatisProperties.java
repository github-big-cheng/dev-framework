package com.wisely.framework.plugins.mybatis;

import com.wisely.framework.helper.ResourceHelper;
import lombok.Getter;
import lombok.Setter;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.core.io.Resource;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * MyBatis配置属性类
 */
@Setter
@Getter
public class MyBatisProperties extends MybatisProperties {

    /**
     * MyBatis映射器文件的地址
     */
    String[] mapperLocations = {"classpath*:/mapper/*.xml"};

    /**
     * 如指定实体类类型，则只扫描指定类型
     */
    private Class<?> typeAliasesSuperType;


    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(this.mapperLocations).orElse(new String[0]))
                .flatMap(location -> Stream.of(ResourceHelper.getResources(location))).toArray(Resource[]::new);
    }
}
