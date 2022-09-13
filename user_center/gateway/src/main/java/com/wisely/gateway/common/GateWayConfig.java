package com.wisely.gateway.common;

import com.wisely.gateway.filter.LogGlobalFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class GateWayConfig {

    @Bean
    @ConfigurationProperties(prefix = "syslog")
    public GateWayLogProperties gateWayLogProperties() {
        return new GateWayLogProperties();
    }


    @Bean
    public LogGlobalFilter logGlobalFilter(GateWayLogProperties properties, KafkaTemplate kafkaTemplate) {
        return new LogGlobalFilter(properties, kafkaTemplate);
    }

}
