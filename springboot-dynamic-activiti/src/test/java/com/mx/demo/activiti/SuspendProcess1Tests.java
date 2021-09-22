package com.mx.demo.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;


/**
 *  单个流程实例（部分数据记录）
 *  流程实例启动与挂起（新老流程变更处理）
 */
@SpringBootTest
public class SuspendProcess1Tests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private RuntimeService runtimeService;
    private RepositoryService repositoryService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        runtimeService = engine.getRuntimeService();
        repositoryService = engine.getRepositoryService();
    }

    /**
     *  单个流程实例挂起
     *  实例挂起不能办理任务，执行报错（org.activiti.engine.ActivitiException: Cannot complete a suspended task）
     */
    @Test
    public void startInstance() {

        // 可以通过id查询，也可以通过key查询(act_ru_execution表)
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("20001").singleResult();
        // 获取流程实例状态
        boolean suspended = processInstance.isSuspended();

        String processInstanceId = processInstance.getId();

        if (suspended) {
            // 已暂停，执行激活操作
            runtimeService.activateProcessInstanceById(processInstanceId);
            log.info("流程定义: {} 激活", processInstanceId);
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            log.info("流程定义: {} 挂起", processInstanceId);
        }
    }
}
