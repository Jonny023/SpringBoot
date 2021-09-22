package com.mx.demo.activiti;

import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;



/**
 *  启动流程实例
 */
@SpringBootTest
public class ActivitiStartInstanceTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private RuntimeService runtimeService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        runtimeService = engine.getRuntimeService();
    }

    /**
     *  受到影响的表
     *  act_hi_actinst      已完成活动信息
     *  act_hi_identitylink 参与者信息
     *  act_hi_procinst     流程实例
     *  act_hi_taskinst     任务实例
     *  act_ru_execution    执行表
     *  act_ru_identitylink 参与者信息
     *  act_ru_task         任务
     */
    @Test
    public void startInstance() {

        // 创建流程实例后，会在act_hi_procinst表中生成一条数据
        // ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave");

        // 流程实例和业务表单绑定，需传入businessKey，businessKey代表请假单的id
        // ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", "businessKey");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", "1001");
        log.info("\r\n流程部署ID: {} \r\n 流程定义ID: {} \r\n 流程实例ID: {} \r\n 活动ID: {}", processInstance.getDeploymentId(),processInstance.getProcessDefinitionId() , processInstance.getId(), processInstance.getActivityId());
        log.info(processInstance.getBusinessKey());
    }
}
