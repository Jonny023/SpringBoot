package com.example.springbootshiro.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis.cache")
public class RedisProperties {

    private int expireSeconds;
    private String clusterNodes;
    private int  connectionTimeout;
    private String password;
    private int soTimeout;
    private int maxAttempts;
}
