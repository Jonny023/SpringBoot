package com.mx.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;

import javax.sql.DataSource;

@SpringBootTest
public class LinkQueryTests {

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
    }

    @Test
    public void linkQuery() {
        ProcessDefinitionQuery processDefinitionQuery = engine.getRepositoryService().createProcessDefinitionQuery();
        ProcessDefinitionQuery processDefinitionQuery1 = processDefinitionQuery.active();
        assertEquals(processDefinitionQuery, processDefinitionQuery1);
    }
}
