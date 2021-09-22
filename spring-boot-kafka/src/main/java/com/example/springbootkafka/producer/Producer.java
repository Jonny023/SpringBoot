package com.example.springbootkafka.producer;

import com.example.springbootkafka.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class Producer {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserService userService;

    /**
     *  每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void execute(){
        log.info("running schedule...");
        userService.async();
    }

}
