package com.mx.demo;

import com.mx.demo.utils.ActivitiUtils;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    ProcessEngine processEngine;

    //动态生成任务实例
    @Test
    public void contextLoads() throws IOException {
        // 1. 创建bpmn模型
        BpmnModel model = new BpmnModel();

        Process process = new Process();
        model.addProcess(process);
        process.setId("my-process");
        process.setName("my-process");

        //创建bpmn元素
        process.addFlowElement(ActivitiUtils.CREATESTARTEVENT());
        process.addFlowElement(ActivitiUtils.CREATEUSERTASK("task1", "提出申请", "张三"));
        process.addFlowElement(ActivitiUtils.CREATEUSERTASK("task2", "经理审批", "李四"));
        process.addFlowElement(ActivitiUtils.CREATEENDEVENT());
        //将各个任务通过连接线连接在一起
        process.addFlowElement(ActivitiUtils.CREATESEQUEBCEFLOW("start", "task1"));
        //同意处理
        process.addFlowElement(ActivitiUtils.CREATESEQUEBCEFLOW("task1", "task2"));
        //不同意处理
        process.addFlowElement(ActivitiUtils.CREATESEQUEBCEFLOW("task2", "task1","不同意","${condition=='不同意'}"));
        process.addFlowElement(ActivitiUtils.CREATESEQUEBCEFLOW("task2", "end","同意","${condition=='同意'}"));

        // 2.生成bpmn自动布局
        new BpmnAutoLayout(model).execute();

        // 3. 部署bpmn模型
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addBpmnModel("dynamic-model.bpmn", model).deploy();

        System.out.println("部署流程成功");

        // 4. 启动流程实例   启动永远是最新版的流程
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey("my-process");
        // 5.发起任务   任务查询（通过任务Id查询任务）
        List<Task> tasks = processEngine.getTaskService().createTaskQuery()
                .processInstanceId(processInstance.getId()).list();
        tasks.forEach(task->{
            System.out.println("任务ID:"+task.getId());
            System.out.println("执行实例ID:"+task.getExecutionId());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务定义的Key:"+task.getTaskDefinitionKey());
            System.out.println("任务办理人:"+task.getAssignee());
            System.out.println("#####################");
        });
//        Assert.assertEquals(1, tasks.size());
//        Assert.assertEquals("First task", tasks.get(0).getName());
//        Assert.assertEquals("fred", tasks.get(0).getAssignee());

        // 6.保存bpmn流程图
        InputStream processDiagram = processEngine.getRepositoryService().getProcessDiagram(processInstance.getProcessDefinitionId());

        FileUtils.copyInputStreamToFile(processDiagram, new File("target/diagram.png"));

        // 7. 保存bpmn.xml的xml类型文件
        InputStream processBpmn = processEngine.getRepositoryService().getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
        FileUtils.copyInputStreamToFile(processBpmn, new File("target/process.bpmn20.xml"));
    }
    //查询流程实例
    @Test
    public void selectExecution(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //创建一个查询对象
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        list.forEach(d ->{
            System.out.println("部署ID:"+d.getId());
            System.out.println("部署名称:"+d.getName());
            System.out.println("部署KEY:"+d.getKey());
            System.out.println("部署CATEGORY:"+d.getCategory());
            System.out.println("部署TENANTID:"+d.getTenantId());
            System.out.println("部署时间:"+d.getDeploymentTime());
            System.out.println("######################");
        });
    }
    //根据处理人查询任务信息
    @Test
    public void checkTaskById(){
        //根据用户人查询用户的任务信息
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee("张三").list();
        tasks.forEach(task->{
            System.out.println("任务ID:"+task.getId());
            System.out.println("执行实例ID:"+task.getExecutionId());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务定义的Key:"+task.getTaskDefinitionKey());
            System.out.println("任务办理人:"+task.getAssignee());
            System.out.println("#####################");
        });
    }
    //完成任务
    @Test
    public void completeTask(){

        //根据任务id完成任务，id来自表act_hi_taskinst
        //processEngine.getTaskService().complete("10002");
        //根据任务添加需要的参数
        Map<String,Object> map = new HashMap<>();
        map.put("condition","同意");
        processEngine.getTaskService().complete("9",map);
        System.out.println("任务完成");
    }
    @Test
    public void completeTask2(){

        //根据任务id完成任务
        processEngine.getTaskService().complete("2503");
        System.out.println("任务完成");
    }
    @Test
    public void testActiviti(){
//        部署流程成功
//        任务ID:9
//        执行实例ID:6
//        流程实例ID:5
//        任务名称:提出申请
//        任务定义的Key:task1
//        任务办理人:张三
    }
}
