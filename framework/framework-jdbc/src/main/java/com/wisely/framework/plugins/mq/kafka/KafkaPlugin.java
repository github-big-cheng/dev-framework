package com.wisely.framework.plugins.mq.kafka;


import com.wisely.framework.plugins.AbstractPlugin;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.*;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.retry.backoff.BackOffPolicyBuilder;
import org.springframework.retry.backoff.SleepingBackOffPolicy;

import java.io.IOException;
import java.time.Duration;

@ConditionalOnProperty(prefix = "plugins.kafka", value = "enabled", havingValue = "true")
@Configuration
@Import({KafkaAnnotationDrivenConfiguration.class, KafkaStreamsAnnotationDrivenConfiguration.class})
public class KafkaPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "KafkaPlugin";
    }

    @Bean
    @ConfigurationProperties(prefix = "plugins.kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    @ConditionalOnMissingBean(KafkaTemplate.class)
    public KafkaTemplate<?, ?> kafkaTemplate(KafkaProperties properties,
                                             ProducerFactory<Object, Object> kafkaProducerFactory,
                                             ProducerListener<Object, Object> kafkaProducerListener,
                                             ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
        return kafkaTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(ProducerListener.class)
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean
    @ConditionalOnMissingBean(ConsumerFactory.class)
    public ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties,
                                                      ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(ProducerFactory.class)
    public ProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties properties,
                                                      ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties());
        String transactionIdPrefix = properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.producer.transaction-id-prefix")
    @ConditionalOnMissingBean
    public KafkaTransactionManager<?, ?> kafkaTransactionManager(ProducerFactory<?, ?> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.jaas.enabled")
    @ConditionalOnMissingBean
    public KafkaJaasLoginModuleInitializer kafkaJaasInitializer(KafkaProperties properties) throws IOException {
        KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
        org.springframework.boot.autoconfigure.kafka.KafkaProperties.Jaas jaasProperties = properties.getJaas();
        if (jaasProperties.getControlFlag() != null) {
            jaas.setControlFlag(jaasProperties.getControlFlag());
        }
        if (jaasProperties.getLoginModule() != null) {
            jaas.setLoginModule(jaasProperties.getLoginModule());
        }
        jaas.setOptions(jaasProperties.getOptions());
        return jaas;
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        KafkaAdmin kafkaAdmin = new KafkaAdmin(properties.buildAdminProperties());
        kafkaAdmin.setFatalIfBrokerNotAvailable(properties.getAdmin().isFailFast());
        return kafkaAdmin;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.retry.topic.enabled")
    @ConditionalOnSingleCandidate(KafkaTemplate.class)
    public RetryTopicConfiguration kafkaRetryTopicConfiguration(KafkaProperties properties, KafkaTemplate<?, ?> kafkaTemplate) {
        org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic retryTopic = properties.getRetry().getTopic();
        RetryTopicConfigurationBuilder builder = RetryTopicConfigurationBuilder.newInstance()
                .maxAttempts(retryTopic.getAttempts()).useSingleTopicForFixedDelays().suffixTopicsWithIndexValues()
                .doNotAutoCreateRetryTopics();
        setBackOffPolicy(builder, retryTopic);
        return builder.create(kafkaTemplate);
    }

    private static void setBackOffPolicy(RetryTopicConfigurationBuilder builder, org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic retryTopic) {
        long delay = (retryTopic.getDelay() != null) ? retryTopic.getDelay().toMillis() : 0;
        if (delay > 0) {
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            BackOffPolicyBuilder backOffPolicy = BackOffPolicyBuilder.newBuilder();
            map.from(delay).to(backOffPolicy::delay);
            map.from(retryTopic.getMaxDelay()).as(Duration::toMillis).to(backOffPolicy::maxDelay);
            map.from(retryTopic.getMultiplier()).to(backOffPolicy::multiplier);
            map.from(retryTopic.isRandomBackOff()).to(backOffPolicy::random);
            builder.customBackoff((SleepingBackOffPolicy<?>) backOffPolicy.build());
        } else {
            builder.noBackoff();
        }
    }
}
