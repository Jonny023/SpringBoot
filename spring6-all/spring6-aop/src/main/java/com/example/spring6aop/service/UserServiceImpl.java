package com.example.spring6aop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String add(String msg) {
        int i = 1/0;
        log.info("添加用户: {}", msg);
        return msg;
    }
}
