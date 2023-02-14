package com.example;

import com.example.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestUser {

    @Test
    public void main() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        //根据id获取bean
        //User user = (User) context.getBean("user");

        //根据类型获取bean (存在多个实例时会报错)
        //User user = context.getBean(User.class);

        //根据id和类型获取
        User user = context.getBean("argUser", User.class);
        System.out.println(user);
        user.run();
    }
}
