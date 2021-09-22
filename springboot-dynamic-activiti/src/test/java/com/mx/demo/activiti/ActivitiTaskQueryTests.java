package com.mx.demo.activiti;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.List;

@SpringBootTest
public class ActivitiTaskQueryTests {

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
     *  查询用户任务列表
     */
    @Test
    public void queryTask1() {

        // 根据流程定义的key，负责人assignee来查询用户任务列表
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee("zhangsan").list();

        for (Task task : taskList) {
            log.info("流程实例ID: {},任务ID: {},任务负责人:{},任务名称: {}", task.getProcessInstanceId(), task.getId(), task.getAssignee(), task.getName());
        }
    }

    /**
     *  查询lisi任务列表
     */
    @Test
    public void queryTask2() {

        Task task = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee("lisi").singleResult();
        // 流程实例ID: 2501,任务ID: 7502,任务负责人:lisi,任务名称: 部门经理审批
        log.info("流程实例ID: {},任务ID: {},任务负责人:{},任务名称: {}", task.getProcessInstanceId(), task.getId(), task.getAssignee(), task.getName());
    }

    /**
     *  查询wangwu任务列表
     */
    @Test
    public void queryTask3() {

        Task task = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee("wangwu").singleResult();
        // 流程实例ID: 2501,任务ID: 10002,任务负责人:wangwu,任务名称: 总经理审批
        log.info("流程实例ID: {},任务ID: {},任务负责人:{},任务名称: {}", task.getProcessInstanceId(), task.getId(), task.getAssignee(), task.getName());
    }
}
