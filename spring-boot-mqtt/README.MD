## Java集成mqtt实现消息发布订阅

### 项目说明

* 项目目录下的mqtt目录为vue的前端项目，实现mqtt消息订阅

> 本项目实现后端通过`http://localhost:8080/api/mqtt/send` 发布消息
> 前端通过vue+mqtt连接mqtt服务实现消息订阅

### 踩坑

* clientId必须唯一，无论前后端都不能同时使用同一个clientId，这东西并不是和用户身份标识相同的东西
* 下面是chatgpt回答的：
  ```shell
    MQTT和WebSocket本身并不是不安全的，但它们都需要在实现时考虑安全性。例如，在使用MQTT时，
  需要使用TLS/SSL加密来保护数据传输的安全性。在使用WebSocket时，也需要使用TLS/SSL加密来保护数据传输的安全性，
  并且需要考虑防止跨站点脚本攻击（XSS）和跨站点请求伪造（CSRF）等安全问题。因此，
  实现MQTT和WebSocket时需要注意安全性，并采取相应的措施来保护数据传输的安全性。
    ```