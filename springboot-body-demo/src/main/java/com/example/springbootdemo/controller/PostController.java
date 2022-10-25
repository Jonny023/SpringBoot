package com.example.springbootdemo.controller;

import com.example.springbootdemo.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class PostController {

    @RequestMapping("/save")
    public String save(@RequestBody User user, HttpServletRequest request) {
        log.info("接口接收参数：{}", user);
        try {
            log.info("接口request parameter：{}", StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }
}
