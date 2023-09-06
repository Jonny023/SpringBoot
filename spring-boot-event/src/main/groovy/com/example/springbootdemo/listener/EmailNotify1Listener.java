package com.example.springbootdemo.listener;

import com.example.springbootdemo.event.EmailNotifyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Jonny
 * @date 创建时间：2023/9/6 9:35
 * @description 邮件监听器(方式2)
 */
@Slf4j
@Component
public class EmailNotify1Listener {

    @EventListener
    public void emailNotify(EmailNotifyEvent event) {
        log.info("发送邮件通知1: {}", event);
    }
}