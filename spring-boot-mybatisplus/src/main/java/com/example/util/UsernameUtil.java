package com.example.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;

@Component
public class UsernameUtil {

    @Resource
    private RedisUtil redisUtil;

    public String getUsername(){
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return (String) redisUtil.get(attrs.getRequest().getHeader("Authorization"));
    }

}
