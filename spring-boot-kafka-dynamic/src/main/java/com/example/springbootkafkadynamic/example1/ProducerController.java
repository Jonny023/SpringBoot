package com.example.springbootkafkadynamic.example1;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("producer")
public class ProducerController {

    @GetMapping("/send")
    public void send() {
        // 动态创建三个生产者
        List<KafkaTemplate> kafkaTemplates = new ArrayList<>();
        String[] servers = {"192.168.56.101:9092","192.168.56.101:9092","192.168.56.101:9092"};
        for (String server : servers) {
            Map<String, Object> producerProps = new HashMap<>();
            producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
            producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer");
            producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer");
            DefaultKafkaProducerFactory pf = new DefaultKafkaProducerFactory<>(producerProps);
            KafkaTemplate kafkaTemplate = new KafkaTemplate(pf, true);
            kafkaTemplates.add(kafkaTemplate);
        }
        // 遍历三个生产者发送消息
        for (KafkaTemplate kafkaTemplate:kafkaTemplates) {
            kafkaTemplate.send("test_topic", "welcome to hangge.com");
        }
    }
}
