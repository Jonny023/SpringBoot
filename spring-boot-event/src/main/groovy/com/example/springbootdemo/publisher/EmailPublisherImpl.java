package com.example.springbootdemo.publisher;

import com.example.springbootdemo.event.EmailNotifyEvent;
import com.example.springbootdemo.event.EmailNotifyEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Jonny
 * @date 创建时间：2023/9/6 9:45
 * @description 邮件事件发布实现
 */
@Slf4j
@Component
public class EmailPublisherImpl implements EmailPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish() {
        log.info("start publish event.");
        EmailNotifyEventMessage message = EmailNotifyEventMessage.builder().msgId(1L).title("验证码通知").content("您的验证码为：1LzB").build();
        applicationEventPublisher.publishEvent(new EmailNotifyEvent(message));
        log.info("finish publish event.");
    }
}