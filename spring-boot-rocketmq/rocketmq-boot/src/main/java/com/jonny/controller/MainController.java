package com.jonny.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
public class MainController {

    public static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private static volatile int i = 1;
    public static final String PAY_TOPIC = "USER_ORDER_PAY";
    public static final String ASYNC_ORDER_MESSAGE = "ASYNC_ORDER_MESSAGE";

    /**
     *  简单发送消息
     * @return
     */
    @GetMapping("/")
    public String index() {
        synchronized (this) {
            String msg = "hello " + i;
            // 简单发送消息（乱序）
            rocketMQTemplate.convertAndSend(PAY_TOPIC, msg);
            log.info("生产消息：{}", msg);
            i++;
            return msg;
        }
    }

    /**
     *  异步发送消息
     * @return
     */
    @GetMapping("/asyncOrder")
    public String asyncOrder() {
        synchronized (this) {
            String msg = "async " + i;
            //异步发送消息
            rocketMQTemplate.asyncSendOrderly(ASYNC_ORDER_MESSAGE, msg, String.valueOf(i), new SendCallback() {

                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("发送成功：{}, 序号：{}", sendResult.getMsgId(), i);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("发送失败：{}", throwable);
                }
            });
            log.info("生产消息：{}", msg);
            i++;
            return msg;
        }
    }

    /**
     *  同步发送消息
     * @return
     */
    @GetMapping("/syncOrder")
    public String syncOrder() {
        synchronized (this) {
            String msg = "sync " + i;
            //同步发送消息
            rocketMQTemplate.syncSendOrderly(ASYNC_ORDER_MESSAGE, msg, String.valueOf(i));
            log.info("生产消息：{}", msg);
            i++;
            return msg;
        }
    }

    @PostMapping(value = "/upload")
    public String upload(String[] files, MultipartFile[] multipartFiles, Demo demo) {
        return Arrays.toString(files);
    }

    public class Demo {

        private String name;

        public Demo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}