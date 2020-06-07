package com.mx.demo;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest
public class ProcessTests {

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private IdentityService identityService;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        identityService = engine.getIdentityService();
        repositoryService = engine.getRepositoryService();
        runtimeService = engine.getRuntimeService();
        taskService = engine.getTaskService();
    }


    /**
     * 启动流程实例
     */
    @Test
    public void deployTest() {
        String applyUserId = "manager";
        identityService.setAuthenticatedUserId(applyUserId);
        // 流程实例key是流程图leave.bpmn中的process id，执行后在act_hi_taskinst表生成一条数据
        runtimeService.startProcessInstanceByKey("leave");
    }

    /**
     *  查询我的待办
     */
    @Test
    public void queryMyToDo() {
        // 根据当前人的ID查询
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("myProcess:1:2504").taskAssignee("manager").list();
        System.out.println(taskList);

        // 查询未签收任务
        List<Task> taskList1 = taskService.createTaskQuery().processDefinitionId("myProcess:1:2504").taskCandidateUser("manager").list();

        List<Task> allTaskLists = new ArrayList<>();
        allTaskLists.addAll(taskList);
        allTaskLists.addAll(taskList1);

        System.out.println(allTaskLists);
        assertEquals(2, taskService.createTaskQuery().count());
        System.out.println(allTaskLists.size());

    }

    /**
     *  查询完成任务
     */
    @Test
    public void queryDoneTask() {

        // 根据当前人的ID查询
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId("myProcess:1:2504").taskAssignee("manager").active().list();
        System.out.println(taskList);

        // 查询未签收任务
        List<Task> taskList1 = taskService.createTaskQuery().processDefinitionId("myProcess:1:2504").taskCandidateUser("manager").active().list();

        List<Task> allTaskLists = new ArrayList<>();
        allTaskLists.addAll(taskList);
        allTaskLists.addAll(taskList1);

        System.out.println(allTaskLists);
        System.out.println(allTaskLists.size());

        Task task = allTaskLists.get(0);

        // 获取流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("leave").singleResult();
        identityService.setAuthenticatedUserId("manager");
        taskService.addComment(task.getId(), processInstance.getId(), "同意");
    }
}
