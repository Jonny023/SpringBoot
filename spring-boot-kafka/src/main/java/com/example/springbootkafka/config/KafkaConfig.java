package com.example.springbootkafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * @description: 消费者配置类
 */
@Configuration
@Slf4j
public class KafkaConfig {

    @Bean("batchKafkaListenerContainerFactory")
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaListenerContainerFactory(
            ConsumerFactory consumerFactory, ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setErrorHandler((e, data) -> log.error("消息消费失败，msg:{},data:{}", e, data));

        // 将批量消息异常处理器添加到参数中
        factory.setBatchErrorHandler((e, data) -> log.error("批量消息消费失败，msg:{},data:{}", e, data));
        factory.setConsumerFactory(consumerFactory);
        configurer.configure(factory, consumerFactory);
        return factory;
    }

}
