package com.mx.demo.uel;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 *  启动流程实例，动态设置assignee
 */
@SpringBootTest
public class AssigneeUEL {


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

    @Test
    public void setupAssignee() {


        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("leave2.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).name("请假流程").deploy();
        try {
            inputStream.close();
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置流程执行人
        Map<String, Object> map = new HashMap<>();
        map.put("assignee1", "zhangsan");
        map.put("assignee2", "lisi");
        map.put("assignee3", "wangwu");

        // 启动流程实例，并设置assignee流程执行人
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", map);
        System.out.println(engine.getName());
    }
}
