# 直播系统（nginx+JavaCV+springboot）



1.自行下载[nginx](https://pan.baidu.com/s/11QQTKuXsaWwQ5wHIV67-Ww)，提取码：`xj6f`
2.下载[服务器状态检查程序nginx-rtmp-rtmp](https://pan.baidu.com/s/1LgxLrD4zh7dWSDi9y7Yk5w)，提取码：`i6hw`

3.在nginx解压目录下的conf文件夹中新建`nginx-win-rtmp.conf`文件

```properties
#user  nobody;
# multiple workers works !
worker_processes  2;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;
 
#pid        logs/nginx.pid;
 
events {
    worker_connections  8192;
    # max value 32768, nginx recycling connections+registry optimization = 
    #   this.value * 20 = max concurrent connections currently tested with one worker
    #   C1000K should be possible depending there is enough ram/cpu power
    # multi_accept on;
}
 
rtmp {
    server {
        listen 2000;
        chunk_size 4000;
        application live {
             live on;
 
             # record first 1K of stream
             record all;
             record_path video;
             record_max_size 1K;
 
             # append current timestamp to each flv
             record_unique on;
 
            #  publish only from localhost
             allow publish 127.0.0.1;
             deny publish all;
 
             #allow play all;
        }
    }
}
 
http {
    include       mime.types;
    default_type  application/octet-stream;
 
    sendfile        off;
    #tcp_nopush     on;
 
    server_names_hash_bucket_size 128;
 
    ## Start: Timeouts ##
    client_body_timeout   10;
    client_header_timeout 10;
    keepalive_timeout     30;
    send_timeout          10;
    keepalive_requests    10;
    ## End: Timeouts ##
 
    #gzip  on;
 
    server {
        listen       80;
        server_name  localhost;
 
 
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }
        location /stat.xsl {
            root nginx-rtmp-module/;
        }
        location /control {
            rtmp_control all;
        }
 
        # For Naxsi remove the single # line for learn mode, or the ## lines for full WAF mode
        location / {
            root   html;
            index  index.html index.htm;
        }
 
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
  
    }
  
}
```



4.启动

```shell
nginx.exe -c conf\nginx-win-rtmp.conf
```



[参考地址](https://blog.csdn.net/Right__/article/details/108081089)

# 下载带rtmp模块的nginx版本，如nginx 1.7.11.3 Gryphon