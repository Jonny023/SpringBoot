package com.example.activitispringboot;

import org.activiti.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = ActivitiSpringBootApplication.class)
class ActivitiSpringBootApplicationTests {

    @Resource
    private RuntimeService runtimeService;

    @Test
    void contextLoads() {

        System.out.println(runtimeService);
    }

}
