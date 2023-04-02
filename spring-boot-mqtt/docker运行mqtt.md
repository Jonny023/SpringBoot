## docker运行mqtt

* 创建文件夹

```sh
 mkdir -p /etc/mqtt/{config,data,log}
```

* 初始化配置

> 必须配置`1883`端口和`9001`端口，不然web端无法通过websocket进行连接，我在vue项目里面通过mqtt连接老是连接不上，结果最终是因为配置文件里面没有配置`9001`的websocket配置，还有个坑就是连接的时候clientId多个端不要不要使用一样的，否则会导致报错，原本我以为这个clientId类似用户身份标识，后端和前端要统一才能实现点对点的功能，结果不是这样的，必须唯一



### 端口说明

* `1883`端口是MQTT协议默认的端口，用于非加密的MQTT连接
* `9001`端口是MQTT over WebSocket协议的默认端口，用于通过WebSocket协议进行加密的MQTT连接

```sh
touch /etc/mqtt/config/pwfile.conf
vi /etc/mqtt/config/mosquitto.conf

persistence true
persistence_location /mosquitto/data
log_dest file /mosquitto/log/mosquitto.log

# 关闭匿名模式
allow_anonymous false
# 指定密码文件
password_file /mosquitto/config/pwfile.conf

# 默认端口是 1883
port 1883

# 以下两行表示可以通过9001端口以websocket的方式连接mosquitto服务
listener 9001
protocol websockets
```

* 配置权限

```sh
chmod -R 755 /etc/mqtt && chmod -R 755 /etc/mqtt/config/

#日志目录要最大权限
chmod -R 777 /etc/mqtt/log
```

* 运行容器

```sh
docker run -it --name=mosquitto --privileged \
-p 1883:1883 -p 9001:9001 \
-v /etc/mqtt/config/mosquitto.conf:/mosquitto/config/mosquitto.conf \
-v /etc/mqtt/config/pwfile.conf:/mosquitto/config/pwfile.conf \
-v /etc/mqtt/data:/mosquitto/data \
-v /etc/mqtt/log:/mosquitto/log \
-d eclipse-mosquitto

# 推荐
docker run -d --name=mosquitto --privileged \
-p 1883:1883 -p 9001:9001 \
-v /etc/mqtt/config/mosquitto.conf:/mosquitto/config/mosquitto.conf \
-v /etc/mqtt/config/pwfile.conf:/mosquitto/config/pwfile.conf \
-v /etc/mqtt/data:/mosquitto/data \
-v /etc/mqtt/log:/mosquitto/log \
eclipse-mosquitto:1.6.14
```

* 生成密码

> 进入容器内部配置密码，记得重启docker容器

```sh
# 进入容器
docker exec -it c169f0ff5aa3 sh

#对于passworf_file，可以复制一份模板，或者创建一个空文件
touch /mosquitto/config/pwfile.conf
chmod -R 755 /mosquitto/config/pwfile.conf

# 使用mosquitto_passwd命令创建用户，其中pwfile.conf为账号管理文件（文件可自定义名称），
# 第一个admin是用户名，第二个admin是密码
mosquitto_passwd -b /mosquitto/config/pwfile.conf admin admin

# 创建用户也可参考下面命令进行操作
```

* 重启容器

```sh
docker restart c169f0ff5aa3
```

* 下载MQTT.fx连接

[官网地址](https://softblade.de/download/)