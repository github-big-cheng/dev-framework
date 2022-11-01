package com.wisely.gateway.common;

import com.wisely.gateway.common.helper.AsyncKafkaBean;
import com.wisely.gateway.filter.LogGlobalFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class GateWayConfig {

    @Bean
    @ConfigurationProperties(prefix = "syslog")
    public GateWayLogProperties gateWayLogProperties() {
        return new GateWayLogProperties();
    }

    @Bean
    public AsyncKafkaBean asyncKafkaBean(KafkaTemplate kafkaTemplate) {
        return new AsyncKafkaBean(kafkaTemplate);
    }

    @Bean
    public LogGlobalFilter logGlobalFilter(GateWayLogProperties properties, AsyncKafkaBean asyncKafkaBean) {
        return new LogGlobalFilter(properties, asyncKafkaBean);
    }


    @Bean
    @ConfigurationProperties(prefix = "gateway.thread-pool")
    public GateWayThreadPoolProperties gateWayThreadPoolProperties() {
        return new GateWayThreadPoolProperties();
    }


    @Bean("taskExecutor")
    public Executor taskExecutor(GateWayThreadPoolProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return executor;
    }

}
