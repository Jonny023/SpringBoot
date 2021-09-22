package com.example.springbootkafka.kafka;

import com.example.springbootkafka.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @description: kafka生产者
 */
@Component
@Slf4j
public class KafkaSender<T> {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(T msg, String topic) {
        try {
            String uuid = UUID.randomUUID().toString();
            ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic, uuid, JsonUtil.toJSONString(msg));
            listenableFuture.addCallback(stringStringSendResult -> {
                if (log.isDebugEnabled()) {
                    log.debug(stringStringSendResult.getProducerRecord().value());
                    log.debug("kafka消息发送成功");
                }
            }, throwable -> log.warn("kafka消息发送失败:{}，{}", uuid, throwable));
        } catch (Exception e) {
            log.error("发送kafka消息异常，{}", e);
        }
    }
}
