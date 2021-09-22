package com.example.service;

import com.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author Jonny
 * @description
 * @date 2019/11/16 0016
 */
@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     *  获取队列中存入的user数据
     *  监听user队列
     * @param user
     */
    @RabbitListener(queues = "user")
    public void register(User user) {
        log.info("用户注册");
        System.out.println(user);
    }

    /**
     *  获取消息头
     *  监听user.news队列
     * @param message
     */
    @RabbitListener(queues = "user.news")
    public void receiveMsg(Message message) {
        System.out.println(message);
        System.out.println(message.getMessageProperties());
        System.out.println(message.getBody());
    }

}
