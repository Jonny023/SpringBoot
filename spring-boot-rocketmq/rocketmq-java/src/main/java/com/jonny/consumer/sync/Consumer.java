package com.jonny.consumer.sync;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

    protected static Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ALI_PAY_GROUP");
        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("ALI_PAY", "*");
        // 注回调实现册类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                log.info(new String(msg.getBody()));
            }
            // 确认消费
            return ConsumeOrderlyStatus.SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
    }
}
