package com.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class PullApplication {
    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PullApplication.class);
        builder.headless(false).run(args);
        System.out.println("---------------启动成功---------------");

        //rtmp服务器拉流地址
        String inputPath = "rtmp://127.0.0.1/live/stream";
        PullStream pullStream = new PullStream();
        pullStream.getPullStream(inputPath);
    }
}