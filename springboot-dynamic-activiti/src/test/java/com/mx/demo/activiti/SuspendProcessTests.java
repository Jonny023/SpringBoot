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
 *  整个流程实例挂起
 *  流程实例启动与挂起（新老流程变更处理）
 */
@SpringBootTest
public class SuspendProcessTests {

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
     *  整个流程实例挂起
     */
    @Test
    public void startInstance() {

        // 可以通过id查询，也可以通过key查询
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();

        // 获取流程实例状态
        boolean suspended = processDefinition.isSuspended();

        String processInstanceId = processDefinition.getId();

        if (suspended) {
            // 已暂停，执行激活操作
            repositoryService.activateProcessDefinitionById(processInstanceId, true, null);
            log.info("流程定义: {} 激活", processInstanceId);
        } else {
            repositoryService.suspendProcessDefinitionById(processInstanceId, true, null);
            log.info("流程定义: {} 挂起", processInstanceId);
        }
    }
}
