package com.example.activitispringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class ActivitiSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiSpringBootApplication.class, args);
    }

}