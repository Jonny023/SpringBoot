package com.example.controller;
import com.alibaba.fastjson.JSON;
import com.example.entity.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Jonny
 * @description
 * @date 2019/11/16 0016
 */
@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ResponseBody
    @RequestMapping("/push")
    public String put() {
        User user = new User(1L, "zhangsan", "风", "123456");
        rabbitTemplate.convertAndSend("myDirectExchange", "hello.direct", JSON.toJSONString(user));
        return "put successful!";
    }

    @ResponseBody
    @RequestMapping("/get")
    public String get() {
        return "get successful!";
    }

}
