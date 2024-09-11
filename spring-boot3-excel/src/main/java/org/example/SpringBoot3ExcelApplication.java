package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringBoot3ExcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3ExcelApplication.class, args);
    }

}
