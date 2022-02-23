package com.example.springbootkafkadynamic.example4;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumer implements Runnable {

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    private Properties props;

    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public Properties getProps() {
        return props;
    }

    public KafkaConsumer(Properties properties, String name) {
        this.props = properties;
        this.topicName = name;
    }


    @Override
    public void run() {
        try {
            //1.创建消费者
            consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(this.props);
            //2.订阅Topic
            //创建一个只包含单个元素的列表，Topic的名字叫作customerCountries
            consumer.subscribe(Collections.singletonList(this.topicName));
            while (true) {
                //消费者是一个长期运行的程序，通过持续轮询向Kafka请求数据。在其他线程中调用consumer.wakeup()可以退出循环
                //在100ms内等待Kafka的broker返回数据.超市参数指定poll在多久之后可以返回，不管有没有可用的数据都要返回
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    LOG.info("kafka消费{}, 信息：{}", record.topic(), record.value());
                }
            }
        } catch (WakeupException e) {
            // ignore for shutdown
            LOG.info("consumer's wakeup is success");
        } finally {
            //退出应用程序前使用close方法关闭消费者，网络连接和socket也会随之关闭，并立即触发一次再均衡
            consumer.close();
        }
    }

    /**
     * kafka consumer 取消订阅topic
     */
    public void shutdown() {
        this.consumer.wakeup();
    }

}
