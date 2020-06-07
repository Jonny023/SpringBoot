package com.mx.demo.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.*;

/**
 *  部署流程定义
 */
@SpringBootTest
public class ActivitiDeploymentTests {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;
    private ProcessEngine engine;
    private IdentityService identityService;
    private RepositoryService repositoryService;

    @BeforeEach
    public void buildEngine() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        engine = configuration.buildProcessEngine();
        identityService = engine.getIdentityService();
        repositoryService = engine.getRepositoryService();
    }

    /**
     *  初始化用户
     */
    public void initUser() {

        User user1 = identityService.newUser("xiaowang");
        user1.setFirstName("小王");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("zhouhao");
        user2.setFirstName("周浩");
        identityService.saveUser(user2);

        assertEquals(2, identityService.createUserQuery().count());
    }

    /**
     *  初始化组
     */
    public void initGroup() {

        Group g1 = identityService.newGroup("manager");
        g1.setName("主管");
        g1.setType("master");
        identityService.saveGroup(g1);

        Group g2 = identityService.newGroup("hr");
        g2.setName("人事");
        g2.setType("hr");
        identityService.saveGroup(g2);

        assertEquals(2, identityService.createGroupQuery().count());
    }

    /**
     *  初始化用户和组关系
     */
    public void initShip() {

        identityService.createMembership("zhouhao", "manager");
        identityService.createMembership("xiaowang", "hr");
    }


    /**
     * 单文件部署流程
     *  act_re_deployment 部署信息0
     *  act_ge_bytearray  流程定义的bpmn和png文件
     *  act_re_procdef    流程定义信息
     */
    @Test
    public void deployTest() {
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("leave.bpmn").addClasspathResource("leave.png").name("请假流程").deploy();
        log.info("id: {}，流程name: {}", deploy.getId(), deploy.getName());
        assertNotNull(deploy);
    }

    /**
     *  以zip压缩包方式部署
     */
    @Test
    public void deployByZip() {

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment().addZipInputStream(zipInputStream).name("请假流程").deploy();
        try {
            inputStream.close();
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("{}", deploy);
    }

    /**
     *  初始化用户数据
     */
    @Test
    public void initUserData() {
        initUser();
        initGroup();
        initShip();
    }

}
