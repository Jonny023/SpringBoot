package com.example.springbootkafkadynamic.example1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public class MyListener {

    private final Logger LOG = LoggerFactory.getLogger(MyListener.class);

    @KafkaListener(topics = "test_topic")
    public void listen(@Payload String data, @Header(KafkaHeaders.GROUP_ID) String groupId) {
        LOG.info("{} : {}", groupId, data);
    }
}