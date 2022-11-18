//package com.example.springbootkafkadynamic.example3;
//
//import com.google.common.base.Splitter;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Objects;
//import java.util.Properties;
//
//@Component
//public class KafkaConsumers implements InitializingBean {
//
//    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumers.class);
//
//    /**
//     * 消费者
//     */
//    private static KafkaConsumer<String, String> consumer;
//
//    /**
//     * topic
//     */
//    private List<String> topicList;
//
//    /**
//     * 初始化消费者
//     *
//     * @param topicList
//     * @return
//     */
//    public KafkaConsumer<String, String> getInitConsumer(List<String> topicList) {
//        //配置信息
//        Properties props = new Properties();
//        //kafka服务器地址
//        props.put("bootstrap.servers", "192.168.56.101:9092");
//        //必须指定消费者组
//        props.put("group.id", "haha");
//        //设置数据key和value的序列化处理类
//        props.put("key.deserializer", StringDeserializer.class);
//        props.put("value.deserializer", StringDeserializer.class);
//
//        //MAX_POLL_RECORDS_CONFIG默认500
//        //props.put("max.poll.records", 1000);
//
//        //创建消息者实例
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        //订阅topic的消息
//        consumer.subscribe(topicList);
//        return consumer;
//    }
//
//    /**
//     * 开启消费者线程
//     * 异常请自己根据需求自己处理
//     */
//    @Override
//    public void afterPropertiesSet() {
//        //读取数据库
//        String topics = "test_topic,test1,test2";
//
//        // 初始化topic
//        topicList = Splitter.on(",").splitToList(Objects.requireNonNull(topics));
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(topicList)) {
//            consumer = getInitConsumer(topicList);
//            // 开启一个消费者线程
//            new Thread(() -> {
//                while (true) {
//                    // 模拟从配置源中获取最新的topic（字符串，逗号隔开）
//                    final List<String> newTopic = Splitter.on(",").splitToList(Objects.requireNonNull(topics));
//                    // 如果topic发生变化
//                    if (!topicList.equals(newTopic)) {
//                        LOG.info("topic 发生变化：newTopic:{},oldTopic:{}-------------------------", newTopic, topicList);
//                        //方法1：重新订阅topic:
//                        //topicList = newTopic;
//                        //consumer.subscribe(newTopic);
//
//                        //方法2：关闭原来的消费者，重新初始化一个消费者
//                        consumer.close();
//                        topicList = newTopic;
//                        consumer = getInitConsumer(newTopic);
//                        continue;
//                    }
//                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//                    //String groupId = consumer.groupMetadata().groupId();
//                    for (ConsumerRecord<String, String> record : records) {
//                        LOG.info("topic: {}, key: {} ,value: {}", record.topic(), record.key(), record.value());
//                    }
//                }
//            }).start();
//        }
//    }
//}