# Sringboot集成jpa和druid多数据源测试连接池

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