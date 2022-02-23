package com.example.springbootkafkadynamic.example4;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final Logger LOG = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final List<KafkaConsumer> kafkaConsumers = Collections.synchronizedList(new ArrayList<>());

    ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
    public Boolean subscribe(KafkaConsumerDTO kafkaConsumerDTO) {
        if (null == kafkaConsumerDTO) {
            return false;
        }

        //已经订阅
        if (alreadyExists(kafkaConsumerDTO)) {
            LOG.warn("消费者：{}已经订阅过了", kafkaConsumerDTO.getTopicName());
            return false;
        }

        LOG.info("订阅topic：{}", kafkaConsumerDTO.getTopicName());
        Properties props = new Properties();

        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerDTO.getBootstrapServers());
        props.put(CommonClientConfigs.GROUP_ID_CONFIG, kafkaConsumerDTO.getGroupId());
        if (!StringUtils.hasText(kafkaConsumerDTO.getKeyDeserializer())) {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        } else {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerDTO.getKeyDeserializer());
        }

        if (!StringUtils.hasText(kafkaConsumerDTO.getValueDeserializer())) {
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        } else {
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerDTO.getValueDeserializer());
        }

        KafkaConsumer kafkaConsumer = new KafkaConsumer(props, kafkaConsumerDTO.getTopicName());
        executor.submit(kafkaConsumer);
        kafkaConsumers.add(kafkaConsumer);
        return true;
    }

    @Override
    public Boolean unSubscribe(KafkaConsumerDTO kafkaConsumerDTO) {
        LOG.info("取消订阅topic：{}", kafkaConsumerDTO.getTopicName());
        Iterator<KafkaConsumer> it = kafkaConsumers.iterator();
        while (it.hasNext()) {
            KafkaConsumer kafkaConsumer = it.next();
            if (Objects.equals(kafkaConsumerDTO.getTopicName(), kafkaConsumer.getTopicName()) && Objects.equals(kafkaConsumerDTO.getBootstrapServers(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG)) && Objects.equals(kafkaConsumerDTO.getGroupId(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.GROUP_ID_CONFIG))) {
                kafkaConsumer.shutdown();
                it.remove();
            }
        }
        return true;
    }

    /**
     * 校验已经存在
     *
     * @param kafkaConsumerDTO
     * @return
     */
    private boolean alreadyExists(KafkaConsumerDTO kafkaConsumerDTO) {
        Iterator<KafkaConsumer> it = kafkaConsumers.iterator();
        while (it.hasNext()) {
            KafkaConsumer kafkaConsumer = it.next();
            if (Objects.equals(kafkaConsumerDTO.getTopicName(), kafkaConsumer.getTopicName()) && Objects.equals(kafkaConsumerDTO.getBootstrapServers(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG)) && Objects.equals(kafkaConsumerDTO.getGroupId(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.GROUP_ID_CONFIG))) {
                return true;
            }
        }
        return false;
    }
}