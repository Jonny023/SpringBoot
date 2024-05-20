package org.example.bpmn;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.example.SpringBootActiviti8Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = SpringBootActiviti8Application.class)
public class DeployTests {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    private static final String processDefinitionKey = "leave_demo";


    @Test
    public void deploy() {
        // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // RepositoryService repositoryService = processEngine.getRepositoryService();

        // 部署流程定义，这会将流程定义加载到流程引擎中
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/leave.bpmn")
                .name("请假审批")
                .deploy();

        // 启动流程引擎，这会加载所有已部署的流程定义
        processEngine.getProcessEngineConfiguration().buildProcessEngine();
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startInstance() {
        ProcessInstance leaveDemo = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        log.info("流程定义id: {}", leaveDemo.getProcessDefinitionId());
        log.info("流程实例id: {}", leaveDemo.getId());
    }

    /**
     * 拾取任务
     */
    @Test
    public void pickTask() {
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).list();
        String assignee = "zhangsan";
        for (Task task : list) {
            taskService.setAssignee(task.getId(), assignee);
            break;
        }
    }

    /**
     * 任务查询
     */
    @Test
    public void queryTasks() {
        String user = "wangwu";
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
                .taskAssignee(user).list();
        for (Task task : list) {
            log.info("任务id: {}, 任务名称：{}", task.getTaskDefinitionKey(), task.getName());
        }
    }

    @Test
    public void completeTask() {
        String taskId = "986353ac-1691-11ef-abd1-0a0027000007";
        Map<String, Object> variables = Maps.newHashMap();
        Map<String, Object> data = Maps.newHashMap();
        data.put("days", 3);
        variables.put("data", data);
        taskService.complete(taskId, variables);
    }
}