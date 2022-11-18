package com.example.springbootjpadruid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootJpaDruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJpaDruidApplication.class, args);
    }

}
