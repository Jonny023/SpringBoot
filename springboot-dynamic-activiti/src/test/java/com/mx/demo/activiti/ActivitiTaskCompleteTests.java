package com.mx.demo.activiti;

import org.activiti.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

/**
 *  完成任务
 *  操作的表
 *  act_hi_actinst
 *  act_hi_identitylink
 *  act_hi_taskinst
 *  act_ru_execution
 *  act_ru_identitylink
 *  act_ru_task
 */
@SpringBootTest
public class ActivitiTaskCompleteTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private TaskService taskService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        taskService = engine.getTaskService();
    }

    /**
     *  张三完成任务（id来自act_ru_task表的id）
     */
    @Test
    public void completeTask1() {

        // 流程实例ID: 2501,任务ID: 2505,任务负责人:zhangsan,任务名称: 填写请假申请表
        // 流程实例ID: 5001,任务ID: 5005,任务负责人:zhangsan,任务名称: 填写请假申请表
        taskService.complete("20005");
    }

    /**
     *  李四完成任务
     */
    @Test
    public void completeTask2() {

        taskService.complete("12502");
    }

    /**
     *  王五完成任务
     */
    @Test
    public void completeTask3() {

        taskService.complete("15002");
    }
}