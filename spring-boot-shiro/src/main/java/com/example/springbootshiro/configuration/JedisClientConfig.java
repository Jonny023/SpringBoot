package com.example.springbootshiro.configuration;

import ml.ytooo.redis.single.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class JedisClientConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public JedisClient getClient() {

        ml.ytooo.redis.RedisProperties.expireSeconds = redisProperties.getExpireSeconds();
        ml.ytooo.redis.RedisProperties.clusterNodes = redisProperties.getClusterNodes();
        ml.ytooo.redis.RedisProperties.connectionTimeout = redisProperties.getConnectionTimeout();
        ml.ytooo.redis.RedisProperties.soTimeout = redisProperties.getSoTimeout();
        ml.ytooo.redis.RedisProperties.maxAttempts = redisProperties.getMaxAttempts();

        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            ml.ytooo.redis.RedisProperties.password = redisProperties.getPassword();
        }else {
            ml.ytooo.redis.RedisProperties.password = null;
        }
        return JedisClient.getInstance();
    }
}
