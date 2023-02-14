package com.example;

import com.example.domain.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class MainTest {

    private Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Test
    public void testUser() {
        ApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        User user = (User) classPathXmlApplicationContext.getBean("user");
        System.out.println(user);

        user.add();

        logger.info("添加用户");
    }

    /**
     * 反射创建对象
     */
    @Test
    public void reflectCreatUser() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = Class.forName("com.example.domain.User");
        //低版本创建对象
        //clazz.newInstance();

        //jdk17反射创建对象
        User user = (User) clazz.getDeclaredConstructor().newInstance();
        System.out.println(user);
        user.add();
    }

    @Test
    public void xx() {
        LocalDate before = LocalDate.parse("2022-02-11", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate now = LocalDate.now();
        System.out.println(Period.between(before, now).getDays());
    }
}
