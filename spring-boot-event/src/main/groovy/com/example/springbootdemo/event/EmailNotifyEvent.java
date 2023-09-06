package com.example.springbootdemo.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Jonny
 * @date 创建时间：2023/9/5 10:48
 * @description 邮件通知事件
 */
public class EmailNotifyEvent extends ApplicationEvent {

    public EmailNotifyEvent(EmailNotifyEventMessage message) {
        super(message);
    }
}