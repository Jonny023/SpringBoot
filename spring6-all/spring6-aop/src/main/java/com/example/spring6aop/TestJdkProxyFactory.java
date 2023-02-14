package com.example.spring6aop;

import com.example.spring6aop.service.UserService;
import com.example.spring6aop.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

public class TestJdkProxyFactory {

    @Test
    public void test() {
        JdkProxyFactory proxyFactory = new JdkProxyFactory(new UserServiceImpl());
        UserService userService = (UserService) proxyFactory.getProxy();
        userService.add("张三");
    }
}