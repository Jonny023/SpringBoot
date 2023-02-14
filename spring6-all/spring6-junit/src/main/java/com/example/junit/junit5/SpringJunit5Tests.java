package com.example.junit.junit5;

import com.example.junit.MainConfig;
import com.example.junit.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * 用@ExtendWith(SpringExtension.class)和@ContextConfiguration(classes = MainConfig.class)
 * 或者@SpringJUnitConfig(MainConfig.class)是一样的
 */
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = MainConfig.class)
@SpringJUnitConfig(MainConfig.class)
public class SpringJunit5Tests {

    @Resource
    private User user;

    @Test
    public void test() {
        user.add();
    }
}
