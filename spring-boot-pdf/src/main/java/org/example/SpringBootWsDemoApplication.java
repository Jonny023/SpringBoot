package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(cn.hutool.extra.spring.SpringUtil.class)
@SpringBootApplication
public class SpringBootWsDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWsDemoApplication.class);
    }

}