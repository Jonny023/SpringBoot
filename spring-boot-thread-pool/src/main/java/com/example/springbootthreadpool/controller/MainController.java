package com.example.springbootthreadpool.controller;

import com.example.springbootthreadpool.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/5/25 9:13
 * @modificed by:
 */
@RestController
public class MainController {

    @Resource
    private AsyncService asyncService;

    @GetMapping("/")
    public void async(){
        asyncService.executeAsync();
    }
}
