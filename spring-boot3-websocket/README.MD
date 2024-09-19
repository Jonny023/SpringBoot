# springboot + websocket集群（redis发布订阅）

> 通过redis发布订阅实现websocket集群session共享

* 访问地址：http://localhost:80/index.html

### 要实现websocket集群，需要注意几个问题
1. redis发布订阅
2. nginx负载均衡
    >    nginx集群配置
    ```shell
    http {
        upstream backend {
          server 127.0.0.1:81;
          server 127.0.0.1:82;
        }
        server {
          listen 80;
          server_name 127.0.0.1;
          location / {
            proxy_pass http://backend;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
    
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
          }
        }
    }
    ```
    3.当所有服务都挂掉的情况下，需要重试，不然页面消息不会变，同4也可以在服务端使用心跳检测工具
    4.服务端每隔一分钟过出现java.io.EOFException: null异常，网上说是nginx超时
    * 可以参考：[地址](https://zhangyuliang1994.github.io/nginx-websocket%E8%BF%9E%E6%8E%A5%E9%94%99%E8%AF%AF-java-io-EOFException-null/index.html)

### 发送消息

```bash
curl -d "msg=好啊&username=admin" "http://127.0.0.1:80/msg/send"
```






## SSL

### 生成证书

```bash
keytool -genkey -alias myssl -keyalg RSA -keystore ./myssl.keystore -storetype pkcs12
```

### 配置

> 端口用443，也可以换成其他端口

```yaml
server:
  port: 443
  ssl:
    enabled: true
    key-store: classpath:/ssl/myssl.keystore
    key-store-password: 123456
    key-store-type: pkcs12
    key-alias: myssl
```

### 访问地址(https://my.com/wss.html)

> wss://my.com/websocket/server/admin

# 注意

* wss访问使用自签证证书，访问需要配置域名，ip:port无法访问，用postman可以连接，浏览器不行，要么配置hosts，要么配置nginx
