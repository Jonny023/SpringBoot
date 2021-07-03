# SpringBoot+Maven多环境配置打包指定文件到指定路径

> 应用场景：项目不同环境的js文件内容不一样，需要根据不同环境自动切换js

### 目录结构

```shell
   resources
   |
   ├─html
   ├─js               # 多环境js源
   │  ├─dev           # 开发环境
   │  │   └─index.js  # js资源
   │  ├─prod          # 生产环境
   │  │   └─index.js  # js资源
   │  └─test          # 测试环境 
   │  │   └─index.js  # js资源
   └─static   # 最终打包来源于static
```

> 解决思路：
> 1. 打包前先通过`maven`的`copy`插件读取maven环境变量`copy`文件到`static`目录
> 2. 再`resources`配置过滤掉指定文件


### 完整配置

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <build.profile.id>dev</build.profile.id>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <build.profile.id>prod</build.profile.id>
        </properties>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <build.profile.id>test</build.profile.id>
        </properties>
    </profile>
</profiles>
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>2.4.1</version>
            <configuration>
                <mainClass>com.example.springbootresourcebuild.SpringbootResourceBuildApplication</mainClass>
            </configuration>
            <executions>
                <execution>
                    <id>repackage</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
                <execution>
                    <id>copy-js</id>
                    <phase>generate-resources</phase>
                    <goals>
                        <goal>run</goal>
                    </goals>
                    <configuration>
                        <tasks>
                            <copy todir="src/main/resources/static" overwrite="true">
                                <fileset dir="src/main/resources/js/${build.profile.id}">
                                    <include name="**/*.js"/>
                                </fileset>
                            </copy>
                            <copy todir="src/main/resources/html" overwrite="true">
                                <fileset dir="src/main/resources/js/${build.profile.id}">
                                    <include name="**/*.html"/>
                                </fileset>
                            </copy>
                        </tasks>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.yml</include>
                <include>**/*.properties</include>
                <include>**/static/*.js</include>
                <include>**/html/*.html</include>
            </includes>
        </resource>
    </resources>
</build>
```

### 打包后目录结构

```shell
├─com
│  └─example
│      └─springbootresourcebuild
├─html
│   └─index.html
└─static
    └─index.js

```

# 注意！！！

通过idea运行时每次都要先编译：执行`mvn clean package`或者`mvn clean compile`都可以，否则编译的文件不是最新的