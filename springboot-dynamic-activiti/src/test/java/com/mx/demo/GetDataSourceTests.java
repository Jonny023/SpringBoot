package com.mx.demo;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
class GetDataSourceTests {


    @Autowired
    private DataSource dataSource;

    private ProcessEngine engine;

    @BeforeEach
    public void getDbSource() {
        System.out.println(dataSource);
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        engine = configuration.buildProcessEngine();
        assertNotNull(engine);
    }

    @Test
    public void afterOption() {
        System.out.println("执行完成.");
    }

}
