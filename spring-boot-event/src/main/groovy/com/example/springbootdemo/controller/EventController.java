package com.example.springbootdemo.controller;

import com.example.springbootdemo.publisher.EmailPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jonny
 * @date 创建时间：2023/9/6 9:49
 * @description 事件
 */
@RequestMapping("/api/event")
@RestController
public class EventController {

    @Autowired
    private EmailPublisher emailPublisher;

    @GetMapping("notify")
    public String eventNotify() {
        emailPublisher.publish();
        return "ok";
    }

}
