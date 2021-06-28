# Sringboot集成jpa和druid多数据源测试连接池填坑

## 时区问题

> 北京时间=UTC+8=GMT+8

* mysql查看数据库时区 `show variables like '%time_zone';`

* 接口返回时间少8小时
    * 方法1 配置`spring.jackson.time-zone: GMT+8`
    * 方法2 类属性配置`@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")`
    
* 数据库时区少8小时，jdbc时区配置：
    * 方法1 `serverTimezone=Asia/Shanghai`
    * 方法2 `serverTimezone=GMT%2B8`
    
* 接口通过json提交数据，日期格式
    * 实体类属性添加`@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")`，该配置受`spring.jackson.time-zone`
    影响，如果不配置会出现时间转换少8小时
      
    ```
    POST /addTest HTTP/1.1
    Host: localhost:8080
    x-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDYxMzc1NTUsInVzZXJuYW1lIjoiYWRtaW4ifQ.ek-Nf_grQ_OsYx8Jf5hnITiTARTl-_xLOeJhXAm27pU
    Content-Type: application/json
    Cache-Control: no-cache
    Postman-Token: 1534acff-8768-3430-7c55-149019c35f5e
    
    {
    "username": "admin",
    "createTime": "2020-11-20 22:22:22"
    }
  ```
  > 如上接口测试到数据库的时间变成了`2020-11-21 06:22:22`，未配置jackson时区多了8小时



# 多数据源初始数分开配置

```yml
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
          master:
            driver-class-name: com.mysql.jdbc.Driver
            url: jdbc:mysql://localhost:3306/stat?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false
            username: root
            password: root
            initial-size: 2
            min-idle: 5
            max-active: 10
            max-wait: 5000
            validationQuery: SELECT 1
            test-on-borrow: false
            test-while-idle: true
            time-between-eviction-runs-millis: 18800
          ck:
            driver-class-name: ru.yandex.clickhouse.ClickHouseDriver
            url: jdbc:clickhouse://localhost:9090/xxx
            username: root
            password: root
            initial-size: 2
            min-idle: 5
            max-active: 10
            max-wait: 5000
            validationQuery: SELECT 1
            test-on-borrow: false
            test-while-idle: true
            time-between-eviction-runs-millis: 18800

          web-stat-filter:
            enabled: true
            exclusions: js,gif,jpg,png,css,ico,/druid/*
          stat-view-servlet:
            enabled: true
            login-username: root
            login-password: 86ac813e-7ddc-44c8-9ead-467480d75fe5

      jpa:
        hibernate:
          ddl-auto: none # update
        show-sql: false
        open-in-view: false
        databasePlatform: org.hibernate.dialect.MySQL57Dialect
        properties:
          hibernate:
            # default_schema: public
            format_sql: true
```

# QueryDSL缓存问题

[参考](https://www.jianshu.com/p/0312c040d407)

> 通过工具直连数据库修改数据，通过querydsl查询出来的数据还是修改前的数据，原因：JPAQueryFactory的session一直是同一个，而JPA执行完后又没有通知JPAQueryFactory的session

### 主数据源

> 千万不要用`return entityManagerFactoryPrimary(builder).getObject().createEntityManager();`创建bean，要用`return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(entityManagerFactoryPrimary(builder).getObject()));`创建，否则通过querydsl查询会有缓存，直接修改数据库数据查询出来还是之前的数据

```java
package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
* 
* 默认数据源
* @author: 
* @author: Jonny
* @date: 2021/5/6 14:04
* @return: 
* @throws: java.lang.Exception
* @modificed by:
*/
@Configuration
@EnableJpaRepositories(
        basePackages = "${spring.jpa.repository}",
        entityManagerFactoryRef = "entityManagerFactoryPrimary",
        transactionManagerRef = "transactionManagerPrimary"
)
@EnableTransactionManagement
public class MasterDataSourceConfig {

    @Value("${spring.jpa.entity}")
    private String entityBasePackage;

    @Lazy
    @Resource(name = "primaryDataSource")
    private DataSource dataSource;

    // JPA扩展配置
    @Resource
    private JpaProperties jpaProperties;

    @Resource
    private HibernateProperties hibernateProperties;

    /**
     * 配置第二个实体管理工厂的bean
     *
     * @return
     */
    @Primary
    @Bean(name = "entityManagerFactoryPrimary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(jpaProperties.getProperties())
                .properties(getVendorProperties())
                .packages(entityBasePackage)
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
    }

    /**
     * EntityManager不过解释，用过jpa的应该都了解
     *
     * @return
     */
    @Primary
    @Bean(name = "entityManagerPrimary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(entityManagerFactoryPrimary(builder).getObject()));
    }

    /**
     * jpa事务管理
     *
     * @return
     */
    @Primary
    @Bean(name = "transactionManagerPrimary")
    public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
    }
}
```

### querydsl查询工厂配置类

```java
package config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;

/**
 * @description:
 * @author: Jonny
 * @date: 2021-02-25
 */
@Configuration
public class JPAQueryFactoryConfig {

    @Bean
    @Primary
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
```

### jpa查询返回值调用set方法会触发update，尽量不要直接修改返回的对象

# 特别注意！！！！！！！！！！！！！！！！！！！！！！

- 创建`EntityManagery`一定要用`SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(entityManagerFactoryPrimary(builder).getObject()));`创建，不然直接修改数据库数据程序查询出来还是修改之前的数据