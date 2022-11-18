package com.example.springbootdockerfile.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/post")
public class PostController {

    @RequestMapping("/")
    public Object index() {
        return "<p>" + LocalDateTime.now() + "\n\r时间戳：" + Instant.now().toEpochMilli() + "\n\r时间戳2：" + System.currentTimeMillis() + "</p>";
    }
}
