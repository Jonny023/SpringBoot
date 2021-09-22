package com.mx.demo;

import org.activiti.engine.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GetServiceTests {

    private ProcessEngine engine;

    @Before
    public void createEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT&useSSL=false");
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        engine = configuration.buildProcessEngine();
        assertNotNull(engine);

    }

    @Test
    public void exe() {

        // 1.Activiti中每一个不同版本的业务流程的定义都需要使用一些定义文件，部署文件和支持数据(例如BPMN2.0XML文件，表单定义文件，流程定义图像文件等)，
        // 这些文件都存储在Activiti内建的Repository中。RepositoryService提供了对repository的存取服务。
        RepositoryService repositoryService = engine.getRepositoryService();
        assertNotNull(repositoryService);

        // 2.在Activiti中，每当一个流程定义被启动一次之后，都会生成一个相应的流程对象实例。RuntimeService提供了启动流程、查询流程实例、设置获取流程实例变量等功能。
        // 此外它还提供了对流程部署，流程定义和流程实例的存取服务。
        RuntimeService runtimeService = engine.getRuntimeService();
        assertNotNull(runtimeService);

        // 3.在Activiti中业务流程定义中的每一个执行节点被称为一个Task，对流程中的数据存取，状态变更等操作均需要在Task中完成。TaskService提供了对用户Task和Form相
        // 它提供了运行时任务查询、领取、完成、删除以及变量设置等功能。
        TaskService taskService = engine.getTaskService();
        assertNotNull(taskService);

        // 4.Activiti中内置了用户以及组管理的功能，必须使用这些用户和组的信息才能获取到相应的Task.IdentityService提供了对Activiti系统中的用户和组的管理功能。
        IdentityService identityService = engine.getIdentityService();
        assertNotNull(identityService);

        // 5.ManagementService提供了对Activiti流程引擎的管理和维护功能，这些功能不在工作流驱动的应用程序中使用，主要用于Activiti系统的日常维护。
        ManagementService managementService = engine.getManagementService();
        assertNotNull(managementService);

        // 6.HistoryService用于获取正在运行或已经完成的流程实例的信息，与RuntimeService中获取的流程信息不同，历史信息包含已经持久化存储的永久信息，并已经被针对查询优化。
        HistoryService historyService = engine.getHistoryService();
        assertNotNull(historyService);
    }


}
