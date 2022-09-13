package com.wisely.framework.plugins.file;

import com.wisely.file.api.impl.FdfsFileApi;
import com.wisely.file.api.FileApi;
import com.wisely.file.api.impl.LocalFileApi;
import com.wisely.file.api.impl.MinIOFileApi;
import com.wisely.file.controller.FileController;
import com.wisely.framework.helper.StringHelper;
import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@ConditionalOnProperty(prefix = "plugins.file", value = "enabled", havingValue = "true")
@Configuration
public class FilePlugin extends AbstractPlugin {


    @Override
    protected String getName() {
        return "FilePlugin";
    }


    @Bean
    @ConfigurationProperties(prefix = "plugins.file")
    public FileProperties fileProperties() {
        return new FileProperties();
    }


    @Bean
    public MultipartConfigElement multipartConfigElement(FileProperties fileProperties) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //允许上传的文件最大值
        factory.setMaxFileSize(DataSize.parse(fileProperties.getMaxFileSize())); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse(fileProperties.getMaxRequestSize()));
        return factory.createMultipartConfig();
    }


    @Bean
    @ConditionalOnMissingBean(FileApi.class)
    public FileApi fileApi(FileProperties fileProperties) {
        if (StringHelper.equals(fileProperties.getMode(), "minio")) {
            return new MinIOFileApi(fileProperties);
        }
        if (StringHelper.equals(fileProperties.getMode(), "fastdfs")) {
            return new FdfsFileApi(fileProperties);
        }

        return new LocalFileApi(fileProperties);
    }

    @Bean
    public FileController fileController() {
        return new FileController();
    }

}
