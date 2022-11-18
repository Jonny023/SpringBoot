package com.example.springbootkafkadynamic.example4;

public interface KafkaService {

    Boolean subscribe(KafkaConsumerDTO kafkaConsumerDTO);

    Boolean unSubscribe(KafkaConsumerDTO kafkaConsumerDTO);
}