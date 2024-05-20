package org.example.service;

import com.google.common.collect.Lists;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessDiagramService {

    @Autowired
    private ProcessEngine processEngine;

    public InputStream generateDiagram(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance != null) {
            BpmnModel bpmnModel = processEngine.getRepositoryService()
                    .getBpmnModel(historicProcessInstance.getProcessDefinitionId());

            List<String> activeActivityIds = new ArrayList<>();
            List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();

            for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                activeActivityIds.add(historicActivityInstance.getActivityId());
            }

            DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();

            return diagramGenerator.generateDiagram(bpmnModel, activeActivityIds, Lists.newArrayList(), "宋体", "宋体", "宋体");
            // return diagramGenerator.generateDiagram(bpmnModel, activeActivityIds, Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), "宋体", "宋体", "宋体", true, "a.png");
        } else {
            // 处理未找到 HistoricProcessInstance 的情况
            return null;
        }
    }
}