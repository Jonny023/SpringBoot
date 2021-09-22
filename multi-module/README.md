# 基于Gradle的多模块多环境（多工程）

* app和user为单独的springboot工程，util为工具类，app实现了多环境打包配置，user非多环境，只是简单的demo示例
* 依赖管理在根目录下的build.gradle中，子工程需要时引入便是，版本号也推荐在父工程管理，这样如果需要升级就不用修改子工程。
* gradleVersion=7