package com.wisely.framework.plugins.mybatis;

import com.wisely.framework.helper.JsonHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;


/**
 * MyBatis插件
 */
@ConditionalOnProperty(prefix = "plugins.mybatis", name = "enabled", havingValue = "true")
@Import({MyBatisPlugin.AutoConfiguredMapperScannerRegistrar.class})
@Slf4j
public class MyBatisPlugin extends AbstractPlugin {


    @Override
    protected String getName() {
        return "myBatisPlugins";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.mybatis")
    public MyBatisProperties myBatisProperties() {
        return new MyBatisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.pagehelper")
    public PageHelperProperties pageHelperProperties() {
        return new PageHelperProperties();
    }

    static class MyBatisConfig implements InitializingBean {
        private final MyBatisProperties properties;
        private final ResourceLoader resourceLoader;
        private final Interceptor[] interceptors;
        private final TypeHandler[] typeHandlers;
        private final LanguageDriver[] languageDrivers;
        private final DatabaseIdProvider databaseIdProvider;
        private final List<ConfigurationCustomizer> configurationCustomizers;

        public MyBatisConfig(
                MyBatisProperties properties,
                ObjectProvider<Interceptor[]> interceptorsProvider,
                ObjectProvider<TypeHandler[]> typeHandlersProvider,
                ObjectProvider<LanguageDriver[]> languageDriversProvider,
                ResourceLoader resourceLoader,
                ObjectProvider<DatabaseIdProvider> databaseIdProvider,
                ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider
        ) {
            this.properties = properties;
            this.interceptors = interceptorsProvider.getIfAvailable();
            this.typeHandlers = typeHandlersProvider.getIfAvailable();
            this.languageDrivers = languageDriversProvider.getIfAvailable();
            this.resourceLoader = resourceLoader;
            this.databaseIdProvider = databaseIdProvider.getIfAvailable();
            this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
        }

        private void checkConfigFileExists() {
            if (this.properties.isCheckConfigLocation() && StringUtils.hasText(this.properties.getConfigLocation())) {
                Resource resource = this.resourceLoader.getResource(this.properties.getConfigLocation());
                Assert.state(resource.exists(),
                        "Cannot find config location: " + resource
                                + " (please add config file or check your Mybatis configuration)"
                );
            }
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            this.checkConfigFileExists();
        }
    }

    @Bean
    public MyBatisPlugin.MyBatisConfig myBatisConfig(
            MyBatisProperties properties,
            ObjectProvider<Interceptor[]> interceptorsProvider,
            ObjectProvider<TypeHandler[]> typeHandlersProvider,
            ObjectProvider<LanguageDriver[]> languageDriversProvider,
            ResourceLoader resourceLoader,
            ObjectProvider<DatabaseIdProvider> databaseIdProvider,
            ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider
    ) {
        return new MyBatisConfig(
                properties, interceptorsProvider, typeHandlersProvider,
                languageDriversProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(
            DataSource dataSource, PageHelperProperties pageHelperProperties, MyBatisConfig config
    ) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();

        //添加pagehelper插件
        try {
            if (Class.forName("com.github.pagehelper.PageHelper") != null) {
                PageInterceptor pageInterceptor = new PageInterceptor();
                Properties properties = JsonHelper.copy(pageHelperProperties.getProperties());
                properties.remove("dialect");
                pageInterceptor.setProperties(properties);
                factory.setPlugins(new PageInterceptor[]{pageInterceptor});
            }
        } catch (Throwable ignored) {
        }

        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(config.properties.getConfigLocation())) {
            factory.setConfigLocation(config.resourceLoader.getResource(config.properties.getConfigLocation()));
        }
        Configuration configuration = config.properties.getConfiguration();
        if (configuration == null && !StringUtils.hasText(config.properties.getConfigLocation())) {
            configuration = new Configuration();
        }
        if (configuration != null && !CollectionUtils.isEmpty(config.configurationCustomizers)) {
            for (ConfigurationCustomizer customizer : config.configurationCustomizers) {
                customizer.customize(configuration);
            }
        }
        factory.setConfiguration(configuration);
        if (config.properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(config.properties.getConfigurationProperties());
        }
        if (!ObjectUtils.isEmpty(config.interceptors)) {
            factory.setPlugins(config.interceptors);
        }
        if (config.databaseIdProvider != null) {
            factory.setDatabaseIdProvider(config.databaseIdProvider);
        }
        if (StringUtils.hasLength(config.properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(config.properties.getTypeAliasesPackage());
        }
        if (config.properties.getTypeAliasesSuperType() != null) {
            factory.setTypeAliasesSuperType(config.properties.getTypeAliasesSuperType());
        }
        if (StringUtils.hasLength(config.properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(config.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(config.typeHandlers)) {
            factory.setTypeHandlers(config.typeHandlers);
        }
        if (!ObjectUtils.isEmpty(config.properties.resolveMapperLocations())) {
            factory.setMapperLocations(config.properties.resolveMapperLocations());
        }
        return factory.getObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory, MyBatisProperties myBatisProperties) {
        ExecutorType executorType = myBatisProperties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    /**
     * 用于扫描@Mapper注解的类
     */
    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(
                AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry
        ) {

            log.debug("Searching for mappers annotated with @Mapper");
            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(this.resourceLoader);
                }

                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                if (log.isDebugEnabled()) {
                    for (String pkg : packages) {
                        log.debug("Using auto-configuration base package '{}'", pkg);
                    }
                }

                scanner.setAnnotationClass(Mapper.class);
                scanner.registerFilters();
                scanner.doScan(StringUtils.toStringArray(packages));
            } catch (IllegalStateException ex) {
                log.debug("无法确定自动配置程序包，自动映射器扫描已禁用。", ex);
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }


}

