package com.example.springbootnacos.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@RestController
public class MainController {

    @GetMapping("/")
    public Object index() {
        System.out.println(new Date());
        System.out.println(LocalDateTime.now());
        System.out.println(LocalTime.now());
        return LocalDateTime.now();
    }
}
