package com.example.springbootrabbitmqproducer.controller;

import com.example.springbootrabbitmqproducer.producer.Producer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@RestController
public class SendController {

    @Resource
    private Producer producer;

    @GetMapping("/")
    public String send() throws Exception {
        producer.send("hello, " + LocalDate.now(), null);
        return "send success. date: "+ LocalDate.now();
    }
}
