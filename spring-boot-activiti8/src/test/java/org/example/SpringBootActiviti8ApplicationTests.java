package org.example;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.assertj.core.util.Lists;
import org.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@SpringBootTest(classes = SpringBootActiviti8Application.class)
class SpringBootActiviti8ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        TaskEntity taskEntity = new TaskEntityImpl();
        taskEntity.setId("0");
        taskEntity.setName("张三");
        taskEntity.setDescription("任务1");
        TaskEntityImpl taskEntity1 = new TaskEntityImpl();
        taskEntity1.setId("-1");
        taskEntity1.setName("张三2");

        List<TaskEntity> tasks = Lists.newArrayList(
                taskEntity,taskEntity1
        );
        userMapper.update(tasks);
    }

}
