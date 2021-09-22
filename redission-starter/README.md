# custom-spring-boot-starter-入门

> 创建自定义starter【redisson-starter】

* 自定义starter步骤
    * 添加依赖
    * 添加配置类
    * 添加spring.factories自动配置信息
    * 添加spring-configuration-metadata.json自动提示

## 自定义starter依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>redisson-starter</artifactId>
    <version>1.0</version>

    <properties>
        <autoconfigure.version>2.0.0.RELEASE</autoconfigure.version>
        <redisson-client.version>3.16.1</redisson-client.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>${autoconfigure.version}</version>
        </dependency>
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>${redisson-client.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```



* 使用
    * 引入依赖
    * 添加配置
    * 注入
    
    

# 注入使用

```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>redisson-starter</artifactId>
    <version>1.0</version>
</dependency>
```

* yml配置

```yaml
redisson:
  host: localhost
  port: 6379
  password: 123456
  database: 14
```

* 使用

```java
@Resource
private RedissonClient redissonClient;
```