package com.example;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

    /**
     * 以zip方式部署【推荐】
     */
    @Test
    public void testZipDeployment() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        System.out.println("流程部署id: " + deployment.getId());
        System.out.println("流程部署名称: " + deployment.getName());
    }

    /**
     * 【第一步】资源文件方式部署
     */
    @Test
    public void testDeployment() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/leave.bpmn")
                //.addClasspathResource("bpmn/leave.my_leave.png") //png|jpg|gif|svg，my_leave作为流程实例id
                .addClasspathResource("bpmn/leave.png") //png|jpg|gif|svg，my_leave作为流程实例id
                .name("请假申请流程")
                .deploy();
        System.out.println("流程部署id: " + deployment.getId());
        System.out.println("流程部署名称: " + deployment.getName());
    }

    /**
     * 查询流程实例
     */
    @Test
    public void testQueryProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey("my_leave").list();
        for (ProcessInstance processInstance : processInstances) {
            System.out.println("流程定义id: " + processInstance.getProcessDefinitionId());
            System.out.println("流程实例id: " + processInstance.getId());
            System.out.println("当前活动id: " + processInstance.getActivityId());
            System.out.println("businessKey: " + processInstance.getBusinessKey());
            System.out.println("====================");
        }
    }

    /**
     * 【第二步】启动流程实例
     */
    @Test
    public void testStartProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_leave");
        System.out.println("流程定义id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id: " + processInstance.getId());
        System.out.println("当前活动id: " + processInstance.getActivityId());
    }

    /**
     * 启动流程实例并绑定业务id
     * businessKey对应数据库的长度为255字符，需注意
     */
    @Test
    public void testStartProcessByBusinessKey() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //添加业务id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_leave", "10001");
        System.out.println("流程定义id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id: " + processInstance.getId());
        System.out.println("当前活动id: " + processInstance.getActivityId());
        System.out.println("businessKey: " + processInstance.getBusinessKey());
    }

    /**
     * 【第三步】查询待办任务
     */
    @Test
    public void testQueryUserTaskList() {
        //String assignee = "worker";
        String assignee = "zhangsan";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("my_leave").taskAssignee(assignee).list();
        for (Task task : list) {
            System.out.println("流程实例id: " + task.getProcessDefinitionId());
            System.out.println("任务id: " + task.getId());
            System.out.println("任务责任人: " + task.getAssignee());
            System.out.println("任务名称: " + task.getName());
        }
    }

    /**
     * 【第四步】完成任务
     */
    @Test
    public void testCompleteUserTask() {
        String assignee = "worker";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("my_leave").taskAssignee(assignee).singleResult();
        //完成任务
        taskService.complete(task.getId());
    }

    /**
     * 删除流程部署
     */
    @Test
    public void deleteDeployment() {
        String deploymentId = "7501";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //未完成的流程不能删除，存在启动流程实例，会报错
        //repositoryService.deleteDeployment(deploymentId);


        //强制级联删除，不会报错，会删除所有历史记录
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 查询bpmn信息
     *
     * @throws IOException
     */
    @Test
    public void testQueryBpmnFile() throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("my_leave").singleResult();
        String deploymentId = processDefinition.getDeploymentId();
        //png图片资
        InputStream pngInputStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
        //bpmn文件流
        InputStream bpmnInputStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
        File pngFile = new File("./leave.png");
        File bpmnFile = new File("./leave.bpmn");
        FileOutputStream pngOutputStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutputStream = new FileOutputStream(bpmnFile);
        IOUtils.copy(pngInputStream, pngOutputStream);
        IOUtils.copy(bpmnInputStream, bpmnOutputStream);
        IOUtils.closeQuietly(pngInputStream);
        IOUtils.closeQuietly(bpmnInputStream);
        IOUtils.closeQuietly(pngOutputStream);
        IOUtils.closeQuietly(bpmnOutputStream);
    }

    /**
     * 查询执行历史
     */
    @Test
    public void testQueryHistory() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        //historicActivityInstanceQuery.processInstanceId("2501");
        historicActivityInstanceQuery.processDefinitionId("my_leave:1:4");
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getProcessDefinitionId());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("=========================");
        }
    }


    /**
     * 挂起所有流程定义
     * 只有流程定义和流程实例可以挂起
     */
    @Test
    public void suspendAllProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("my_leave")
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        String processDefinitionId = processDefinition.getId();
        if (suspended) {
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义id:" + processDefinitionId + ", 已激活");
        } else {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义id:" + processDefinitionId + ", 已挂起");
        }
    }

    /**
     * 挂起、激活实例
     */
    @Test
    public void suspendProcessInstance() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("my_leave")
                .singleResult();
        boolean suspended = processInstance.isSuspended();
        String instanceId = processInstance.getId();
        if (suspended) {
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + ", 已激活");
        } else {
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + ", 已挂起");
        }
    }

}
