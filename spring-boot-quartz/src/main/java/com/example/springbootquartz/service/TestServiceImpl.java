package com.example.springbootquartz.service;

import org.springframework.stereotype.Service;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/7/8 18:21
 * @modificed by:
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public void hello() {
        System.out.println("call TestServiceImpl...");
    }
}
