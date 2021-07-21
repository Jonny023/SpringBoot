package com.example.springbootkafka.controller;

import com.example.springbootkafka.base.ResultDTO;
import com.example.springbootkafka.base.ResultEnum;
import com.example.springbootkafka.entity.User;
import com.example.springbootkafka.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@RestController
public class MainController {

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserService userService;

    @GetMapping("/")
    public WebAsyncTask index() {
        User user = new User().setId(UUID.randomUUID().toString()).setSex('男').setUsername(String.valueOf(System.currentTimeMillis()));
        WebAsyncTask<ResultDTO> webAsyncTask = new WebAsyncTask(10000L, threadPoolTaskExecutor, () -> {
            userService.send(user);
            log.info(Thread.currentThread().getName());
            if (log.isDebugEnabled()) {
                log.debug(user.toString());
            }
            return ResultDTO.ok();
        });

        webAsyncTask.onTimeout(() -> {
            if (log.isWarnEnabled()) {
                log.warn("超时:{}", user);
            }
            return ResultDTO.create().fail(ResultEnum.TIMEOUT);
        });

        webAsyncTask.onError(() -> {
            if (log.isErrorEnabled()) {
                log.error("异常:{}", user);
            }
            return ResultDTO.create().fail(ResultEnum.EXCEPTION);
        });
        return webAsyncTask;
    }
}
