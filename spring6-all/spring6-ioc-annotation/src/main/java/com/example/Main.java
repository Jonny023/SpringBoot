package com.example;

import com.example.cnofig.SpringConfig;
import com.example.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        User user = ac.getBean("user", User.class);
        user.add();
    }
}
