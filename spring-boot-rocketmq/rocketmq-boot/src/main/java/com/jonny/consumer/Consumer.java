package com.jonny.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RocketMQMessageListener(consumerGroup = "userOrderConsumer", topic = "USER_ORDER_PAY")
public class Consumer implements RocketMQListener {

    protected static final Logger log = LoggerFactory.getLogger(Consumer.class);
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onMessage(Object o) {
        log.info("消费信息：{} --- {}", o, FORMATTER.format(LocalDateTime.now()));
    }
}