package com.example.config;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Component
public class ActivitiConfig {

    @Resource
    private DataSource dataSource;

    @Lazy
    @Resource
    private ProcessEngine processEngine;

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration() {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource);
        springProcessEngineConfiguration.setTransactionManager(dataSourceTransactionManager());
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        return springProcessEngineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    public TaskService taskService() {
        return processEngine.getTaskService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine.getRuntimeService();
    }

    @Bean
    public HistoryService historyService() {
        return processEngine.getHistoryService();
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine.getRepositoryService();
    }

    @Bean
    public ManagementService managementService() {
        return processEngine.getManagementService();
    }
}
