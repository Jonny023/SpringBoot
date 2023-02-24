package com.example;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class TestCandidate {

    private String key = "candidate";

    @Test
    public void deploy() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/candidate.bpmn")
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
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).singleResult();
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
    }

    @Test
    public void testStartProcess() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        runtimeService.startProcessInstanceByKey(key);
    }

    @Test
    public void queryTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "lisi";
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey(key)
                .taskCandidateUser(assignee)
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
     * 认领/拾取任务
     */
    @Test
    public void claimTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "lisi";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskCandidateUser(assignee)
                .singleResult();
        if (task != null) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            System.out.println(task.getProcessVariables());
            System.out.println(task.getTaskLocalVariables());
            taskService.claim(task.getId(), assignee);
            System.out.println(assignee + "认领任务：" + task.getId());
        }
    }

    /**
     * 归还、转办任务
     */
    @Test
    public void revertTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "lisi";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            System.out.println(task.getProcessVariables());
            System.out.println(task.getTaskLocalVariables());
            taskService.setAssignee(task.getId(), null);
            //任务转办，任务领取后处理不了，转交给其他人处理
            //taskService.setAssignee(task.getId(), "周星驰");
            System.out.println(assignee + "归还任务：" + task.getId());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String assignee = "lisi";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            taskService.complete(task.getId());
        }
    }
}
