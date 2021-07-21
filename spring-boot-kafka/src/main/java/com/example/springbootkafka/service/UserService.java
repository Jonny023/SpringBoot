package com.example.springbootkafka.service;

import com.example.springbootkafka.constrains.Const;
import com.example.springbootkafka.entity.User;
import com.example.springbootkafka.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Resource
    private KafkaSender kafkaSender;

    /**
     *  发送到kafka
     * @param user
     */
    public void send(User user) {
        kafkaSender.send(user, Const.USER_TOPIC);
    }

    /**
     *  异步发送
     */
    @Async("taskExecutor")
    public void async() {
        log.info("异步请求：{}", Thread.currentThread().getName());
        User user = new User().setId(UUID.randomUUID().toString()).setSex('男').setUsername(String.valueOf(System.currentTimeMillis()));
        send(user);
    }
}
