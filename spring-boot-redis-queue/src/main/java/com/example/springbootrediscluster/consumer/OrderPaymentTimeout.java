package com.example.springbootrediscluster.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderPaymentTimeout implements RedisDelayQueueHandler<Map> {
    
    private final Logger LOG = LoggerFactory.getLogger(OrderPaymentTimeout.class);

    @Override
    @Async
    public void execute(Map map) {
        LOG.info("(收到订单支付超时延迟消息) {}", map);
    }
}