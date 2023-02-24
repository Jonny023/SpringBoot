package com.example;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGateway {

    @Test
    public void deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/gateway.bpmn")
                //.addClasspathResource("bpmn/leave.my_leave.png") //png|jpg|gif|svg，my_leave作为流程实例id
                //.addClasspathResource("bpmn/leave.png") //png|jpg|gif|svg，my_leave作为流程实例id
                .name("请假申请流程")
                .deploy();
        System.out.println("流程部署id: " + deployment.getId());
        System.out.println("流程部署名称: " + deployment.getName());
    }

    @Test
    public void deleteDeploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("my_leave_gateway").singleResult();
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
    }

    @Test
    public void testGateway() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        String key = "my_leave_gateway";
        Map<String, Object> variables = new HashMap<String, Object>();
        //用对象作为流程实例变量必须实现序列化接口Serializable
        Evection evection = new Evection();
        evection.setNum(2D);
        variables.put("evection", evection);
        variables.put("assignee0", "张三");
        variables.put("assignee1", "李四");
        variables.put("assignee2", "总经理");
        variables.put("assignee3", "财务");

        runtimeService.startProcessInstanceByKey(key, variables);
    }

    @Test
    public void queryTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String key = "my_leave_gateway";
        String assignee = "财务";
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(key)
                //.taskAssignee(assignee)
                .list();
        for (Task task : list) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            System.out.println(task.getProcessVariables());
            System.out.println(task.getTaskLocalVariables());
            System.out.println("==========================");
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String key = "my_leave_gateway";
        String assignee = "财务";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            taskService.complete(task.getId());
        }
    }
}
