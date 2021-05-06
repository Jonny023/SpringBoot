package com.example.springbootclickhouse.config;

import com.example.springbootclickhouse.base.BaseRepositoryImpl;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
//@EntityScan(basePackages = "com.example.springbootclickhouse.entity")
@EnableJpaRepositories(
//        basePackages = "${spring.jpa.repository}", //可通过spEL表达式动态配置
        basePackages = "com.example.springbootclickhouse.repository",
        entityManagerFactoryRef = "ckEntityManagerFactoryBean",
        transactionManagerRef = "ckTransactionManager",
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class JpaClickHouseConfig {

    /**
     *  数据源名称为：
     *
     * @return
     */
    @Bean(name = "ckDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ck")
    public DataSource ckDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Resource(name = "ckDataSource")
    private DataSource dataSource;

    // JPA扩展配置
    @Resource
    private JpaProperties jpaProperties;

    // 实体管理工厂
    @Resource
    private EntityManagerFactoryBuilder factoryBuilder;

    @Resource
    private HibernateProperties hibernateProperties;

    /**
     * 配置第二个实体管理工厂的bean
     *
     * @return
     */
    @Bean(name = "ckEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        return factoryBuilder.dataSource(dataSource)
                .properties(getVendorProperties())
                .packages("com.example.springbootclickhouse.entity")
                .persistenceUnit("ckPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        Map<String, String> properties = jpaProperties.getProperties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return hibernateProperties.determineHibernateProperties(properties, new HibernateSettings());
    }

    /**
     * EntityManager不过解释，用过jpa的应该都了解
     *
     * @return
     */
    @Bean(name = "ckEntityManager")
    public EntityManager entityManager() {
        return entityManagerFactoryBean().getObject().createEntityManager();
    }

    /**
     * jpa事务管理
     *
     * @return
     */
    @Bean(name = "ckTransactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }
}
