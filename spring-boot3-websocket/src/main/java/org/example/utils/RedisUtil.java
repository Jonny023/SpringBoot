package org.example.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 发布
     *
     * @param key 频道
     */
    public void publish(String key, String value) {
        redisTemplate.convertAndSend(key, value);
    }
}