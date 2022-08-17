# Springboot 自定义数据源

[参考](https://www.daimajiaoliu.com/daima/4797c5d119003fe)

> 动态读取数据源: 默认配置一个主数据源，用于存放用户保存的数据库数据源

## 准备

### sql脚本

```sql
-- 建表语句
-- demo.sys_db definition
CREATE TABLE `sys_db` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `type` varchar(20) DEFAULT NULL COMMENT '类型：mysql',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='数据源连接';


-- 数据
INSERT INTO sys_db (`name`, `url`, `username`, `password`, `type`) VALUES ('test1', 'jdbc:mysql://localhost:3306/test1?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull', 'root', 'root', 'mysql');
INSERT INTO sys_db (`name`, `url`, `username`, `password`, `type`) VALUES ('test2', 'jdbc:mysql://localhost:3306/test2?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull', 'root', 'root', 'mysql');
```

### 依赖

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.2</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-1.2-api</artifactId>
    <version>2.18.0</version>
</dependency>

```

### yaml

```yaml
spring:
#  aop:
#    proxy-target-class: true #true为使用CGLIB代理
  datasource:
    #nullCatalogMeansCurrent=true&
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull
    username: root
    password: root
    #新版mysql驱动配置方法
    driverClassName: com.mysql.cj.jdbc.Driver
    ###################以下为druid增加的配置###########################
    type: com.alibaba.druid.pool.DruidDataSource
    # 下面为连接池的补充设置，应用到上面所有数据源中
    # 初始化大小，最小，最大
    initialSize: 5
    minIdle: 5
    maxActive: 20
    # 配置获取连接等待超时的时间
    maxWait: 60000
    # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    timeBetweenEvictionRunsMillis: 60000
    # 配置一个连接在池中最小生存的时间，单位是毫秒
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    # 打开PSCache，并且指定每个连接上PSCache的大小
    poolPreparedStatements: true
    maxPoolPreparedStatementPerConnectionSize: 20
    # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    filters: stat,wall,log4j
    # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    # 合并多个DruidDataSource的监控数据
    useGlobalDataSourceStat: true
    ###############以上为配置druid添加的配置########################################

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true #打开驼峰命名
  mapper-locations: classpath*:/mapper/**/*.xml
```

### 配置类


* DruidDataSourceConfiguration

```java
package com.example.app.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonny
 */
@Configuration
@EnableTransactionManagement
public class DruidDataSourceConfiguration {

    @Primary
    @Bean("mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSourceBuilder().build();
    }

    /**
     * 注册一个StatViewServlet    druid监控页面配置1-帐号密码配置
     *
     * @return servlet registration bean
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 注册一个：filterRegistrationBean   druid监控页面配置2-允许页面正常浏览
     *
     * @return filter registration bean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDebug(false);
        //配置缺省的数据源
        // 默认数据源配置 DefaultTargetDataSource
        dynamicDataSource.setDefaultTargetDataSource(dataSource());
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        //额外数据源配置 TargetDataSources
        targetDataSources.put("mainDataSource", dataSource());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        //解决手动创建数据源后字段到bean属性名驼峰命名转换失效的问题
        sqlSessionFactoryBean.setConfiguration(configuration());

        // 设置mybatis的主配置文件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // Resource mybatisConfigXml = resolver.getResource("classpath:mybatis/mybatis-config.xml");
        //  sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
        // 设置别名包
        //  sqlSessionFactoryBean.setTypeAliasesPackage("com.testdb.dbsource.pojo");
        //就是这句代码，只能指定单个mapper.xml文件，加通配符的话找不到文件
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    /**
     * 读取驼峰命名设置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis-plus.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }
}
```
* DynamicDataSource

```java
package com.example.app.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.example.app.domain.entity.SysDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Set;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private boolean debug = true;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Object, Object> dynamicTargetDataSources;
    private Object dynamicDefaultTargetDataSource;


    @Override
    protected Object determineCurrentLookupKey() {
        Long datasource = DBContextHolder.getDataSource();
        if (datasource != null) {
            Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
            if (dynamicTargetDataSources2.containsKey(datasource)) {
                log.info("---当前数据源：" + datasource + "---");
            } else {
                log.info("不存在的数据源：");
                return null;
            }
        } else {
            log.info("---当前数据源：默认数据源---");
        }

        return datasource;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.dynamicTargetDataSources = targetDataSources;
    }


    /**
     * 创建数据源
     *
     * @param key
     * @param driveClass
     * @param url
     * @param username
     * @param password
     * @param type
     * @return
     */
    public boolean createDataSource(Long key, String driveClass, String url, String username, String password, String type) {
        try {
            try {
                // 排除连接不上的错误
                Class.forName(driveClass);
                // 相当于连接数据库
                DriverManager.getConnection(url, username, password);
            } catch (Exception e) {

                return false;
            }
            @SuppressWarnings("resource")
            //HikariDataSource druidDataSource = new HikariDataSource();
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setName(key.toString());
            druidDataSource.setDriverClassName(driveClass);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setInitialSize(50); //初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
            druidDataSource.setMaxActive(200); //最大连接池数量
            druidDataSource.setMaxWait(60000); //获取连接时最大等待时间，单位毫秒。当链接数已经达到了最大链接数的时候，应用如果还要获取链接就会出现等待的现象，等待链接释放并回到链接池，如果等待的时间过长就应该踢掉这个等待，不然应用很可能出现雪崩现象
            druidDataSource.setMinIdle(40); //最小连接池数量
            String validationQuery = "select 1 from dual";
            //if("mysql".equalsIgnoreCase(databasetype)) {
            //    driveClass = DBUtil.mysqldriver;
            //    validationQuery = "select 1";
            //} else if("oracle".equalsIgnoreCase(databasetype)){
            //    driveClass = DBUtil.oracledriver;
            //    druidDataSource.setPoolPreparedStatements(true); //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
            //    druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(50);
            //    int sqlQueryTimeout = ADIPropUtil.sqlQueryTimeOut();
            //    druidDataSource.setConnectionProperties("oracle.net.CONNECT_TIMEOUT=6000;oracle.jdbc.ReadTimeout="+sqlQueryTimeout);//对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒
            //} else if("sqlserver2000".equalsIgnoreCase(databasetype)){
            //    driveClass = DBUtil.sql2000driver;
            //    validationQuery = "select 1";
            //} else if("sqlserver".equalsIgnoreCase(databasetype)){
            //    driveClass = DBUtil.sql2005driver;
            //    validationQuery = "select 1";
            //}
            druidDataSource.setTestOnBorrow(true); //申请连接时执行validationQuery检测连接是否有效，这里建议配置为TRUE，防止取到的连接不可用
            druidDataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
            druidDataSource.setValidationQuery(validationQuery); //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
            druidDataSource.setFilters("stat");//属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
            druidDataSource.setTimeBetweenEvictionRunsMillis(60000); //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            druidDataSource.setMinEvictableIdleTimeMillis(180000); //配置一个连接在池中最小生存的时间，单位是毫秒，这里配置为3分钟180000
            druidDataSource.setKeepAlive(true); //打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
            druidDataSource.setRemoveAbandoned(true); //是否移除泄露的连接/超过时间限制是否回收。
            druidDataSource.setRemoveAbandonedTimeout(3600); //泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时
            druidDataSource.setLogAbandoned(true); ////移除泄露连接发生是是否记录日志
            druidDataSource.init();
            this.dynamicTargetDataSources.put(key, druidDataSource);
            setTargetDataSources(this.dynamicTargetDataSources);// 将map赋值给父类的TargetDataSources
            super.afterPropertiesSet();// 将TargetDataSources中的连接信息放入resolvedDataSources管理
            log.info(key + "数据源初始化成功");
            //log.info(key+"数据源的概况："+druidDataSource.dump());
            return true;
        } catch (Exception e) {
            log.error(e + "");
            return false;
        }
    }

    /**
     * 删除数据源
     *
     * @param dataSourceId
     * @return
     */
    public boolean delDatasources(Long dataSourceId) {
        Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
        if (dynamicTargetDataSources2.containsKey(dataSourceId)) {
            Set<DruidDataSource> druidDataSourceInstances = DruidDataSourceStatManager.getDruidDataSourceInstances();
            for (DruidDataSource l : druidDataSourceInstances) {
                if (dataSourceId.toString().equals(l.getName())) {
                    dynamicTargetDataSources2.remove(dataSourceId);
                    DruidDataSourceStatManager.removeDataSource(l);
                    // 将map赋值给父类的TargetDataSources
                    setTargetDataSources(dynamicTargetDataSources2);
                    // 将TargetDataSources中的连接信息放入resolvedDataSources管理
                    super.afterPropertiesSet();
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 测试数据源连接是否有效
     *
     * @param key
     * @param driveClass
     * @param url
     * @param username
     * @param password
     * @return
     */
    public boolean testDatasource(Long key, String driveClass, String url, String username, String password) {
        try {
            Class.forName(driveClass);
            DriverManager.getConnection(url, username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        this.dynamicDefaultTargetDataSource = defaultTargetDataSource;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @return the dynamicTargetDataSources
     */
    public Map<Object, Object> getDynamicTargetDataSources() {
        return dynamicTargetDataSources;
    }

    /**
     * @param dynamicTargetDataSources the dynamicTargetDataSources to set
     */
    public void setDynamicTargetDataSources(Map<Object, Object> dynamicTargetDataSources) {
        this.dynamicTargetDataSources = dynamicTargetDataSources;
    }

    /**
     * @return the dynamicDefaultTargetDataSource
     */
    public Object getDynamicDefaultTargetDataSource() {
        return dynamicDefaultTargetDataSource;
    }

    /**
     * @param dynamicDefaultTargetDataSource the dynamicDefaultTargetDataSource to set
     */
    public void setDynamicDefaultTargetDataSource(Object dynamicDefaultTargetDataSource) {
        this.dynamicDefaultTargetDataSource = dynamicDefaultTargetDataSource;
    }

    public void createDataSourceWithCheck(SysDb dataSource) throws Exception {
        Long datasourceId = dataSource.getId();
        log.info("正在检查数据源：" + datasourceId);
        Map<Object, Object> dynamicTargetDataSources2 = this.dynamicTargetDataSources;
        if (dynamicTargetDataSources2.containsKey(datasourceId)) {
            log.info("数据源" + datasourceId + "之前已经创建，准备测试数据源是否正常...");
            //DataSource druidDataSource = (DataSource) dynamicTargetDataSources2.get(datasourceId);
            DruidDataSource druidDataSource = (DruidDataSource) dynamicTargetDataSources2.get(datasourceId);
            boolean rightFlag = true;
            Connection connection = null;
            try {
                log.info(datasourceId + "数据源的概况->当前闲置连接数：" + druidDataSource.getPoolingCount());
                long activeCount = druidDataSource.getActiveCount();
                log.info(datasourceId + "数据源的概况->当前活动连接数：" + activeCount);
                if (activeCount > 0) {
                    log.info(datasourceId + "数据源的概况->活跃连接堆栈信息：" + druidDataSource.getActiveConnectionStackTrace());
                }
                log.info("准备获取数据库连接...");
                connection = druidDataSource.getConnection();
                log.info("数据源" + datasourceId + "正常");
            } catch (Exception e) {
                //把异常信息打印到日志文件
                log.error(e.getMessage(), e);
                rightFlag = false;
                log.info("缓存数据源" + datasourceId + "已失效，准备删除...");
                if (delDatasources(datasourceId)) {
                    log.info("缓存数据源删除成功");
                } else {
                    log.info("缓存数据源删除失败");
                }
            } finally {
                if (null != connection) {
                    connection.close();
                }
            }
            if (rightFlag) {
                log.info("不需要重新创建数据源");
                return;
            } else {
                log.info("准备重新创建数据源...");
                createDataSource(dataSource);
                log.info("重新创建数据源完成");
            }
        } else {
            createDataSource(dataSource);
        }

    }

    private void createDataSource(SysDb dataSource) throws Exception {
        Long datasourceId = dataSource.getId();
        log.info("准备创建数据源" + datasourceId);
        String databasetype = dataSource.getType();
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();
        String url = dataSource.getUrl();
        String driveClass = "com.mysql.cj.jdbc.Driver";
        //if("mysql".equalsIgnoreCase(databasetype)) {
        //    driveClass = DBUtil.mysqldriver;
        //} else if("oracle".equalsIgnoreCase(databasetype)){
        //    driveClass = DBUtil.oracledriver;
        //}  else if("sqlserver2000".equalsIgnoreCase(databasetype)){
        //    driveClass = DBUtil.sql2000driver;
        //} else if("sqlserver".equalsIgnoreCase(databasetype)){
        //    driveClass = DBUtil.sql2005driver;
        //}
        if (testDatasource(datasourceId, driveClass, url, username, password)) {
            boolean result = this.createDataSource(datasourceId, driveClass, url, username, password, databasetype);
            if (!result) {
                log.error("数据源" + datasourceId + "配置正确，但是创建失败");
                //throw new ADIException("数据源"+datasourceId+"配置正确，但是创建失败",500);
            }
        } else {
            log.error("数据源配置有错误");
            //throw new ADIException("数据源配置有错误",500);
        }
    }

}
```
* DBContextHolder

```java
package com.example.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jonny
 */
public class DBContextHolder {

    private final static Logger log = LoggerFactory.getLogger(DBContextHolder.class);

    /**
     * 对当前线程的操作-线程安全的
     */
    private static final ThreadLocal<Long> contextHolder = new ThreadLocal<Long>();

    /**
     * 调用此方法，切换数据源
     *
     * @param dataSource
     */
    public static void setDataSource(Long dataSource) {
        contextHolder.set(dataSource);
        log.info("已切换到数据源:{}", dataSource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static Long getDataSource() {
        return contextHolder.get();
    }

    /**
     * 删除数据源
     */
    public static void clearDataSource() {
        contextHolder.remove();
        log.info("已切换到主数据源");
    }
}

```

### 实体类

* SysDb

```java
package com.example.app.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysDb {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String type;
}

```

* User

```java
package com.example.app.domain.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private Long id;
    private String username;
}
```

### mapper

* UserMapper
```java
package com.example.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> list();
}


```

* resources/mapper/UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.app.mapper.UserMapper">

    <!--查询所有用户信息-->
    <select id="list" resultType="com.example.app.domain.entity.User">
        select * from user
    </select>

</mapper>
```

* DataSourceMapper

```java
package com.example.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.domain.entity.SysDb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataSourceMapper extends BaseMapper<SysDb> {

    @Select("SELECT * FROM sys_db")
    List<SysDb> list();
}
```

### Service

* UserService

```java
package com.example.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app.domain.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> list();
}

```

* DBChangeService

```java
package com.example.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app.domain.entity.SysDb;

import java.util.List;

public interface DBChangeService extends IService<SysDb> {

    List<SysDb> list();

    boolean changeDb(Long datasourceId) throws Exception;
}

```

### ServiceImpl

* UserServiceImpl

```java
package com.example.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app.domain.entity.User;
import com.example.app.mapper.UserMapper;
import com.example.app.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<User> list() {
        return baseMapper.list();
    }
}

```

* DBChangeServiceImpl

```java
package com.example.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app.config.DBContextHolder;
import com.example.app.config.DynamicDataSource;
import com.example.app.domain.entity.SysDb;
import com.example.app.mapper.DataSourceMapper;
import com.example.app.service.DBChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBChangeServiceImpl extends ServiceImpl<DataSourceMapper, SysDb> implements DBChangeService {

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<SysDb> list() {
        return dataSourceMapper.list();
    }

    @Override
    public boolean changeDb(Long datasourceId) throws Exception {

        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();

        List<SysDb> dataSourcesList = dataSourceMapper.list();

        for (SysDb dataSource : dataSourcesList) {
            if (dataSource.getId().equals(datasourceId)) {
                System.out.println("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getId());
                //创建数据源连接&检查 若存在则不需重新创建
                dynamicDataSource.createDataSourceWithCheck(dataSource);
                //切换到该数据源
                DBContextHolder.setDataSource(dataSource.getId());
                return true;
            }
        }
        return false;

    }

}
```

### controller

```java
package com.example.app.controller;

import com.example.app.config.DBContextHolder;
import com.example.app.domain.entity.User;
import com.example.app.service.DBChangeService;
import com.example.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private DBChangeService dbChangeServiceImpl;
    @Resource
    UserService userService;

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/test/{id}")
    public  String test(@PathVariable("id") Long id) throws Exception {

        //切换到数据库dbtest2
        dbChangeServiceImpl.changeDb(id);
        List<User> userList= userService.list();
        System.out.println(userList.toString());

        //再切换到数据库dbtest3
        dbChangeServiceImpl.changeDb(id);
        List<User> userList3= userService.list();
        System.out.println(userList3.toString());

        //切回主数据源
        DBContextHolder.clearDataSource();
        return "ok";
    }

}
```