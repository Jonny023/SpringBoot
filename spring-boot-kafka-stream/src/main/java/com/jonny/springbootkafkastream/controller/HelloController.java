package com.jonny.springbootkafkastream.controller;

import com.alibaba.fastjson.JSON;
import com.jonny.springbootkafkastream.entity.User;
import com.jonny.springbootkafkastream.service.IHelloService;
import com.jonny.springbootkafkastream.utils.ThreadPoolService;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/4/23 9:27
 * @modificed by:
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

  @Resource
  private IHelloService helloService;

  private int random() {
    int ages[] = {18,24,19, 22, 30};
    int len = ages.length;
    return ages[new Random().nextInt(len)];
  }

  @GetMapping("/send")
  public String send() {
    ExecutorService es = ThreadPoolService.getEs();
    for (int i = 0; i < 20; i++) {
//      int index = i;
        User u = new User().setUsername("user" + i).setArea("ChongQing" + i).setAge(random());
        helloService.send(JSON.toJSONString(u));
    }
    return "send success";
  }

}
