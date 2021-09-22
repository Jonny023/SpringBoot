# 应用

* [参考文档](https://my.oschina.net/tangdu/blog/1625336#comment-list)
* [对应springboot依赖管理版本](https://plugins.gradle.org/plugin/org.springframework.boot)
* [子工程依赖](https://docs.gradle.org/current/userguide/plugins.html#sec:subprojects_plugins_dsl)
* [gradle task](https://www.jianshu.com/p/cd1a78dc8346)


* 指定环境打包
    * dev - 开发
    * test - 测试
    * prod - 生产
    > gradle bootJar -Denv=prod

* 运行/编译`app`模块时在`idea`中编辑`bootJar`，在`VM Options`中指定打包环境`-Denv=test` 
 
### 构建任务

> 执行bootJar时依赖buildJar，执行buildJar时先执行copyEnv

* 功能描述
  * 打包指定环境的文件，prod/dev/test为不同环境目录，将不同环境的配置及文件创建好放入到目录中

```groovy
task copyEnv(type: Copy) {
    from "src/main/env/" + System.properties['env']
    into "src/main/resources"
    dependsOn 'clean'
}

task buildJar(type: Copy) {
    from "src/main/env/" + System.properties['env']
    into "build/resources/main"
    dependsOn 'copyEnv'
}

bootJar.dependsOn buildJar
```

# 不能在父工程执行编译，需要在对应的目录模块执行bootJar