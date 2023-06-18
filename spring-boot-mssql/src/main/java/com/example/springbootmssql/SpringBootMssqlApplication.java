package com.example.springbootmssql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springbootmssql.mapper")
public class SpringBootMssqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMssqlApplication.class, args);
    }

}