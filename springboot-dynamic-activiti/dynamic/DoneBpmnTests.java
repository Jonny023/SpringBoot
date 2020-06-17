package com.gd;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gd.common.service.ImgService;
import com.gd.common.util.activiti.ActivitiUtils;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GDmainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoneBpmnTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Test
    public void create() throws IOException {

        String processId = "leave";

        // 1. 创建bpmn模型
        BpmnModel model = new BpmnModel();

        Process process = new Process();
        model.addProcess(process);
        process.setId(processId);
        process.setName("请假");

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
        Deployment deployment = repositoryService.createDeployment().addBpmnModel("dynamic-model.bpmn", model).deploy();

        System.out.println("部署流程成功");

        // 4. 启动流程实例   启动永远是最新版的流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);

        // 5.发起任务   任务查询（通过任务Id查询任务）
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        tasks.forEach(task->{
            System.out.println("任务ID:"+task.getId());
            System.out.println("执行实例ID:"+task.getExecutionId());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务定义的Key:"+task.getTaskDefinitionKey());
            System.out.println("任务办理人:"+task.getAssignee());
            System.out.println("#####################");
        });

        // 6.保存bpmn流程图
        InputStream processDiagram = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        FileUtils.copyInputStreamToFile(processDiagram, new File("target/diagram.png"));

        // 7. 保存bpmn.xml的xml类型文件
        InputStream processBpmn = repositoryService.getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
        FileUtils.copyInputStreamToFile(processBpmn, new File("target/process.bpmn"));

        // BPMN文件转json
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        ObjectNode objectNode = jsonConverter.convertToJson(model);
        System.out.println(objectNode.toString());

    }

    /**
     *  完成任务
     */
    @Test
    public void actTask() {
        String taskId = "9";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("condition", "同意");
            taskService.setVariable(taskId, "condition", "同意");
            taskService.complete(taskId);
            System.out.println("任务完成");
        }
    }

    @Autowired
    private ImgService imgService;

    /**
     *  根据流程实例获取流程历史
     *  创建历史流程高亮图
     * @throws Exception
     */
    @Test
    public void makeImg() throws Exception {
        String processInstanceId = "5";
        byte[] imgBytes = imgService.getFlowImgByProcInstId(processInstanceId);
        OutputStream outputStream = new FileOutputStream("d:\\process_img.png");
        outputStream.write(imgBytes);
        outputStream.close();
        System.out.println("流程图创建完成");
    }
}
