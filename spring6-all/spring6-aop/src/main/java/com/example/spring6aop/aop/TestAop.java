package com.example.spring6aop.aop;

import com.example.spring6aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(MainConfig.class);
        UserService userService = ac.getBean(UserService.class);
        userService.add("张三");
    }
}