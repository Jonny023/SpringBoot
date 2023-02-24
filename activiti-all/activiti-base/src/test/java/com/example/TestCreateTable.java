package com.example;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class TestCreateTable {

    /**
     * 创建数据库表
     */
    @Test
    public void testCreateTable() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //读取配置
        //ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
        //processEngineConfiguration.buildProcessEngine();

        System.out.println(processEngine);
    }
}
