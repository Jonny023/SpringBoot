package com.example.springbootkafka.consumer;

import com.example.springbootkafka.constrains.Const;
import com.example.springbootkafka.entity.User;
import com.example.springbootkafka.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费者
 */
@Component
@Slf4j
public class Consumer {

    @KafkaListener(topics = {Const.USER_TOPIC}, groupId = Const.USER_CONSUMER, containerFactory = "batchKafkaListenerContainerFactory")
    public void consumerAll(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("{},消费了{}条数据", Const.USER_CONSUMER, records.size());

        List<User> rawDataList = new ArrayList<>(records.size());

        records.stream().forEach(data -> {
            try {
                User rawData = JsonUtil.toClass(data.value().toString(), User.class);
                if (rawData != null) {
                    rawDataList.add(rawData);
                }
            } catch (Exception e) {
                log.warn(String.format("消息反序列化异常，丢弃，data:%s,message：{}", data), e);
            }
        });
        try {
            //TODO 业务处理后收掉调用消费【手动确认】，异常时不消费
            ack.acknowledge();
        } catch (Exception e) {
            log.error("consumerAll保存失败，message：{}", e);
        }
    }
}
