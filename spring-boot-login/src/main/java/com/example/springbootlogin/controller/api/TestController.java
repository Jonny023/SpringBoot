package com.example.springbootlogin.controller.api;

import com.example.springbootlogin.domain.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String test() {
        log.info("当前登录用户：{}", UserContext.username());
        return "api test success";
    }
}
