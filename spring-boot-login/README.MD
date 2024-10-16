## Springboot+JWT实现前台、后台登录分开处理


### 前台请求拦截器
> 拦截路由：/api/**
* com.example.springbootlogin.interceptor.ApiInterceptor
    * 登录地址：http://localhost:8080/api/login
      * 用户：demo
      * 密码：123456
    * 测试拦截：http://localhost:8080/api/test
        * 请求头携带token Authorization token头

### 后台请求拦截
> 拦截路由：/bg/**
* com.example.springbootlogin.interceptor.BackgroundInterceptor
    * 登录地址：http://localhost:8080/bg/login
        * 用户：admin
        * 密码：123456
    * 测试拦截：http://localhost:8080/bg/currentUser
        * 请求头携带token Authorization token头