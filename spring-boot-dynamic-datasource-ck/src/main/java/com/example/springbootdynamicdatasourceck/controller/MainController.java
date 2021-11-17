package com.example.springbootdynamicdatasourceck.controller;

import com.example.springbootdynamicdatasourceck.service.Event2Service;
import com.example.springbootdynamicdatasourceck.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MainController {

    @Resource
    private EventService eventService;
    @Resource
    private Event2Service event2Service;

    @GetMapping("/db1")
    public Object db1() {
        return eventService.queryAll();
    }

    @GetMapping("/db2")
    public Object db2() {
        return event2Service.queryAll();
    }
}
