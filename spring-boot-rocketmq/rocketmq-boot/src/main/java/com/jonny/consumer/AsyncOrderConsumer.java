package com.jonny.consumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  顺序消费
 */
@Component
@RocketMQMessageListener(consumerGroup = "asyncOrderConsumer", topic = "ASYNC_ORDER_MESSAGE", consumeMode = ConsumeMode.ORDERLY)
public class AsyncOrderConsumer implements RocketMQListener {

    protected static final Logger log = LoggerFactory.getLogger(AsyncOrderConsumer.class);
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onMessage(Object o) {
        log.info("消费信息：{} --- {}", o, FORMATTER.format(LocalDateTime.now()));
    }
}