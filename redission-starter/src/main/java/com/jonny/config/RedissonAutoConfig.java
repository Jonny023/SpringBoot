package com.jonny.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnClass(RedissonAutoConfig.class)
public class RedissonAutoConfig {

    @Resource
    private RedissonProperties prop;

    /**
     *  stand-alone mode
     * @return
     */
    @Bean
    public RedissonClient getRedisson(){
        Assert.notNull(prop, "Redisson config is not null.");
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s:%s", prop.getHost(), prop.getPort()))
                .setDatabase(prop.getDatabase())
                .setPassword(prop.getPassword());
        return Redisson.create(config);
    }
}