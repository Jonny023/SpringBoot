package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVariables {

    /**
     * 启动流程实例传入流程变量1
     */
    @Test
    public void testStartProcessByVariable1() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        String key = "my_leave";
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

    /**
     * 启动流程实例传入流程变量2
     */
    @Test
    public void testStartProcessByVariable2() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        String key = "my_leave";
        Map<String, Object> variables = new HashMap<String, Object>();
        Evection evection = new Evection();
        evection.setNum(3D);
        variables.put("evection", evection);
        variables.put("assignee0", "张三1");
        variables.put("assignee1", "李四1");
        variables.put("assignee2", "总经理1");
        variables.put("assignee3", "财务1");

        runtimeService.startProcessInstanceByKey(key, variables);
    }

    @Test
    public void queryTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String key = "my_leave";
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
        String key = "my_leave";
        String assignee = "李四1";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            taskService.complete(task.getId());
        }
    }

    /**
     * 完成任务并指定审批人
     */
    @Test
    public void completeTask1() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        String key = "my_leave";
        String assignee = "总经理1";
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();
        if (task != null) {
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("assignee3", "admin");
            taskService.complete(task.getId(), variables);
            //taskService.setVariable();
            //taskService.setVariableLocal();
            //taskService.setVariables();
        }
    }
}
