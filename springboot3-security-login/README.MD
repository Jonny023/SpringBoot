## springboot3集成spring security多种登录方式

> 本文简单实现多种登录方式，简略版，底层好多没实现，这里只是作为参考demo，有些需要修改的地方请自行修改

* 目录结构
  ![](./img/module.png)


### 多种登录方式
* 用户名+密码
  > 处理器：com.example.security.handler.impl.UsernamePasswordAuthHandler
    ```shell
    curl --location 'localhost:8080/api/auth/login' \
    --header 'Content-Type: application/json' \
    --data '{
        "username": "jonny",
        "password": "123456"
    }'
    ```

* 邮箱+验证码

  > 处理器：com.example.security.handler.impl.EmailPasswordAuthHandler

  ```sh
  curl --location 'localhost:8080/api/auth/email' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "email": "123@email.com",
      "password": "123456"
  }'
  ```

* 手机号+验证码

  > 处理器：com.example.security.handler.impl.PhoneCodeAuthHandler

  ```sh
  curl --location 'localhost:8080/api/auth/phone' \
  --header 'Content-Type: application/json' \
  --data '{
      "phone": "13000000000",
      "code": "123456"
  }'
  ```

### 测试是否授权

```sh
curl --location 'localhost:8080/api/test/get' \
--header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzMwNDc5MjQxNzYsInVzZXJJZCI6MSwiaWF0IjoxNzMyNDQzMTI0MTc2LCJ1c2VybmFtZSI6Impvbm55In0.vIXnOXkoU0E1qFpx45kLqGWFgX53V-V46jc82-bzJsc'
```

### 放行参数配置

> 放行的请求在`com.example.security.SecurityConfig.EXCLUDE_URLS`中配置，也可以自己配置到配置文件中

* com.example.security.SecurityConfig.EXCLUDE_URLS，有两个地方会用到，SecurityConfig类和JwtAuthenticationFilter过滤器