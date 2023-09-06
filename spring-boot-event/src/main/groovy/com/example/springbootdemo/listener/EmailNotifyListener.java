package com.example.springbootdemo.listener;

import com.example.springbootdemo.event.EmailNotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Jonny
 * @date 创建时间：2023/9/6 9:35
 * @description 邮件监听器(方式1)
 */
@Slf4j
@Component
public class EmailNotifyListener implements ApplicationListener<EmailNotifyEvent> {

    @Override
    public void onApplicationEvent(EmailNotifyEvent event) {
        log.info("发送邮件通知: {}", event);
    }
}