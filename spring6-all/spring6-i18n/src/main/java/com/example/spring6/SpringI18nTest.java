package com.example.spring6;

import com.example.spring6.config.SpringResourceI18nConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.Locale;

public class SpringI18nTest {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringResourceI18nConfig.class);
        Object[] objs = new Object[]{"高启强", LocalDateTime.now().toString()};
        String username = ac.getMessage("user.username", null, Locale.US);
        System.out.println(username);
        String message = ac.getMessage("user.tips", objs, Locale.CHINA);
        System.out.println(message);
    }
}
