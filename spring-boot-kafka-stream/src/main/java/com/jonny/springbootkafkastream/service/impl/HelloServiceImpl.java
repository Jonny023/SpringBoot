package com.jonny.springbootkafkastream.service.impl;


import com.jonny.springbootkafkastream.service.IHelloService;
import javax.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/4/23 9:28
 * @modificed by:
 */
@Service
public class HelloServiceImpl implements IHelloService {

  @Resource
  private KafkaTemplate kafkaTemplate;

  /**
   * 发送数据
   */
  @Override
  public void send(String data) {
    kafkaTemplate.send("topic_1", data);
  }

}
