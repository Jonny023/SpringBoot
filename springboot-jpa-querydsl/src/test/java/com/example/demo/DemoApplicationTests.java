package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    void contextLoads() {

        User user = userService.get("admin");
        System.out.println(user);
    }

    @Test
    void saveOrUpdate() {
        User user = new User();
        user.setNickname("Jonny");
        user.setUsername("guest");
        user.setPassword("123456");
        User newUser = userService.save(user);
        System.out.println(newUser);
    }

}
