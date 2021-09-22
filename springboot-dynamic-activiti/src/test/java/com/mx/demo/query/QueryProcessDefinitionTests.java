package com.mx.demo.query;


import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.List;

/**
 *  查询流程定义
 */
@SpringBootTest
public class QueryProcessDefinitionTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private RepositoryService repositoryService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        repositoryService = engine.getRepositoryService();
    }

    /**
     *  查询流程定义基本信息
     */
    @Test
    public void queryProcessDefinitionKey() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.processDefinitionKey("leave").orderByDeploymentId().desc().list();
        processDefinitions.forEach(p -> log.info("流程定义ID: {}, 流程定义名称: {}, 流程定义的Key: {}, 版本号: {}, 流程部署ID: {}", p.getId(), p.getName(), p.getKey(), p.getVersion(), p.getDeploymentId()));
    }
}
