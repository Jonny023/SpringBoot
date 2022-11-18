package com.mx.demo.delete;


import org.activiti.engine.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

/**
 *  查询流程定义
 */
@SpringBootTest
public class DeleteProcessDefinitionTests {

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
     *  删除已部署流程
     *  受影响的表：
     *  act_re_deployment 部署信息0
     *  act_ge_bytearray  流程定义的bpmn和png文件
     *  act_re_procdef    流程定义信息
     */
    @Test
    public void deleteProcessDefinition() {
        // 参数是流程部署的ID
//        repositoryService.deleteDeployment("2501");

        // 流程任务节点为执行完，想要删除流程定义，需要执行强制删除（级联删除），传入true，默认为false
        repositoryService.deleteDeployment("2501", true);
    }
}
