package com.example.springstudy;

import com.example.springstudy.core.BootConfig;
import com.example.springstudy.service.TestService;
import com.example.springstudy.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BootConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService.test());

        TestService testService = (TestService) context.getBean("testService");
        System.out.println(testService);
    }
}