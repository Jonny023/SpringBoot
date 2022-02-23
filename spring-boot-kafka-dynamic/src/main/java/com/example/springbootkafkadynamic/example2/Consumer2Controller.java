package com.example.springbootkafkadynamic.example2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping("/consumer2")
public class Consumer2Controller {

    private final Logger LOG = LoggerFactory.getLogger(Consumer2Controller.class);

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     * 消费者配置属性
     *
     * @param brokersCommaSep 多个服务器逗号分隔
     * @param group           组id
     * @param autoCommit      自动提交
     * @return properties.
     */
    public static Map<String, Object> consumerProps(String brokersCommaSep, String group, String autoCommit) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersCommaSep);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 启动消费者
     *
     * @return
     */
    @GetMapping("/start")
    public String start() {
        // 在不使用“@KafkaListener”注解的情况下启动代理
        Map<String, Object> consumerProps = consumerProps("192.168.56.101:9092", "test_1", "false");
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        ContainerProperties containerProperties = new ContainerProperties("test_topic");
        KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(cf, containerProperties);
        final BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) record -> {
            LOG.warn("topic: {}, offset: {},  msg: {}", record.topic(), record.offset(), record.value());
            records.add(record);
        });
        container.start();
        return "done";
    }

    @GetMapping("/stop")
    public String stop() {
        MessageListenerContainer listenerContainer = registry.getListenerContainer("test_1");
        if (listenerContainer.isContainerPaused()) {

        }
        return "stop success";
    }
}