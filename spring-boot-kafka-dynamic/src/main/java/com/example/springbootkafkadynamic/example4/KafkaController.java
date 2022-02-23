package com.example.springbootkafkadynamic.example4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafkaConsumer")
public class KafkaController {

    @Resource
    private KafkaService kafkaService;

    private String TOPIC = "test_topic";

    /**
     *  启动消费
     * @return
     */
    @GetMapping("/start")
    public String start() {
        KafkaConsumerDTO dto = new KafkaConsumerDTO();
        dto.setBootstrapServers("192.168.56.101:9092");
        dto.setGroupId("test_1");
        dto.setTopicName(TOPIC);
        kafkaService.subscribe(dto);
        return "start success";
    }

    /**
     *  停止消费
     * @return
     */
    @GetMapping("/stop")
    public String stop() {
        KafkaConsumerDTO dto = new KafkaConsumerDTO();
        dto.setBootstrapServers("192.168.56.101:9092");
        dto.setGroupId("test_1");
        dto.setTopicName(TOPIC);
        kafkaService.unSubscribe(dto);
        return "stop success";
    }
}
