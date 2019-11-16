package com.example.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jonny
 * @description 注册JSON转换器
 * @date 2019/11/16 0016
 */
@Configuration
public class AmqpConfig {

    @Bean
    public Jackson2JsonMessageConverter Object2JSONString() {
        return new Jackson2JsonMessageConverter();
    }
}
