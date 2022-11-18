package com.demo;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class PushApplication {
    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PushApplication.class);
        builder.headless(false).run(args);
        System.out.println("---------------启动成功---------------");

        //设置rtmp服务器推流地址
        String outputPath = "rtmp://127.0.0.1:2000/live/stream";
        RecordPush recordPush = new RecordPush();
        recordPush.getRecordPush(outputPath, 25);
    }

}
