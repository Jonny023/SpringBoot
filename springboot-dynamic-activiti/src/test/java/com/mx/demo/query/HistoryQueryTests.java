package com.mx.demo.query;


import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
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
 *  历史数据查询
 */
@SpringBootTest
public class HistoryQueryTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private HistoryService historyService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        historyService = engine.getHistoryService();
    }

    /**
     *  查询历史记录
     */
    @Test
    public void queryProcessDefinitionKey() {
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 根据流程实例id查询历史，并按照startTime升序排序
        List<HistoricActivityInstance> list = historicActivityInstanceQuery.processInstanceId("10001").orderByHistoricActivityInstanceStartTime().asc().list();
        list.forEach(d-> log.info("{}, {}, {}, {}", d.getActivityId(), d.getActivityName(), d.getProcessDefinitionId(), d.getProcessInstanceId()));
    }
}
