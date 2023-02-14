package com.example.junit.domain;

import org.springframework.stereotype.Component;

@Component
public class User {

    public void add() {
        System.out.println("添加用户");
    }
}
