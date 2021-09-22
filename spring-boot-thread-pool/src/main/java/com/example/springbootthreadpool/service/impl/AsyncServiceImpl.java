package com.example.springbootthreadpool.service.impl;

import com.example.springbootthreadpool.service.AsyncService;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/5/25 9:08
 * @modificed by:
 */
@Log
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync() {
        log.info("start executeAsync");

        System.out.println("异步处理业务");
        System.out.println("线程名称：" + Thread.currentThread().getName());

        log.info("end executeAsync");
    }
}
