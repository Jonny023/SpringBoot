# Kafka动态订阅

> 场景：很多用户自己定义自己的kafka服务地址，自定义topic进行消费

* [参考地址1](https://javamana.com/2021/06/20210619121700398M.html)
* [参考地址2](https://blog.csdn.net/zhou_fan_xi/article/details/105241273)
* [参考地址3](https://stackoverflow.com/questions/46400329/how-to-pass-topics-dynamically-to-a-kafka-listener)
* [参考地址4【自定义】](https://blog.csdn.net/qq_35457078/article/details/88838511)
* [参考地址5【自定义】](https://www.codeleading.com/article/80102973019/)

## 动态订阅消费

### 1.yaml
```java
@KafkaListener(topics = {"${xxx.xxx}"})
```
### 2.db
```java
@Autowired
private TopicService topicService;

@KafkaListener(topics = "#{topicService.get()}")
public void listener() {}
```

### 3.自定义代码
```java
@RestController
@RequestMapping("/consumer2")
public class Consumer2Controller {

    private final Logger LOG = LoggerFactory.getLogger(Consumer2Controller.class);

    /**
     *  消费者配置属性
     * @param brokersCommaSep 多个服务器逗号分隔
     * @param group         组id
     * @param autoCommit   自动提交
     * @return properties.
     */
    public static Map<String, Object> consumerProps(String brokersCommaSep, String group, String autoCommit) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersCommaSep);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     *  启动消费者
     * @return
     */
    @GetMapping("/start")
    public String start() {
        // 在不使用“@KafkaListener”注解的情况下启动代理
        Map<String, Object> consumerProps = consumerProps("192.168.56.101:9092", "test_1", "false");
        DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        ContainerProperties containerProperties = new ContainerProperties("test_topic");
        KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(cf, containerProperties);
        final BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) record -> {
            //LOG.warn("Message received: {}", record);
            LOG.warn("Message received: {}", record.value());
            records.add(record);
        });
        container.start();
        return "done";
    }
}
```

### 4.原生客户端【推荐】

```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>2.3.0</version>
</dependency>
```

#### DTO
```java
package com.example.springbootkafkadynamic.example4;

public class KafkaConsumerDTO {

    private String topicName;
    private String bootstrapServers;
    private String groupId;
    private String keyDeserializer;
    private String valueDeserializer;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }
}
```

#### 线程消费者

```java
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
```

#### Service

```java
package com.example.springbootkafkadynamic.example4;

public interface KafkaService {

    Boolean subscribe(KafkaConsumerDTO kafkaConsumerDTO);

    Boolean unSubscribe(KafkaConsumerDTO kafkaConsumerDTO);
}
```

#### ServiceImpl

```java
package com.example.springbootkafkadynamic.example4;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final Logger LOG = LoggerFactory.getLogger(KafkaServiceImpl.class);

    private final List<KafkaConsumer> kafkaConsumers = Collections.synchronizedList(new ArrayList<>());

    ExecutorService executor = Executors.newFixedThreadPool(5);

    @Override
    public Boolean subscribe(KafkaConsumerDTO kafkaConsumerDTO) {
        if (null == kafkaConsumerDTO) {
            return false;
        }

        //已经订阅
        if (alreadyExists(kafkaConsumerDTO)) {
            LOG.warn("消费者：{}已经订阅过了", kafkaConsumerDTO.getTopicName());
            return false;
        }

        LOG.info("订阅topic：{}", kafkaConsumerDTO.getTopicName());
        Properties props = new Properties();

        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerDTO.getBootstrapServers());
        props.put(CommonClientConfigs.GROUP_ID_CONFIG, kafkaConsumerDTO.getGroupId());
        if (!StringUtils.hasText(kafkaConsumerDTO.getKeyDeserializer())) {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        } else {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerDTO.getKeyDeserializer());
        }

        if (!StringUtils.hasText(kafkaConsumerDTO.getValueDeserializer())) {
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        } else {
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerDTO.getValueDeserializer());
        }

        KafkaConsumer kafkaConsumer = new KafkaConsumer(props, kafkaConsumerDTO.getTopicName());
        executor.submit(kafkaConsumer);
        kafkaConsumers.add(kafkaConsumer);
        return true;
    }

    @Override
    public Boolean unSubscribe(KafkaConsumerDTO kafkaConsumerDTO) {
        LOG.info("取消订阅topic：{}", kafkaConsumerDTO.getTopicName());
        Iterator<KafkaConsumer> it = kafkaConsumers.iterator();
        while (it.hasNext()) {
            KafkaConsumer kafkaConsumer = it.next();
            if (Objects.equals(kafkaConsumerDTO.getTopicName(), kafkaConsumer.getTopicName()) && Objects.equals(kafkaConsumerDTO.getBootstrapServers(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG)) && Objects.equals(kafkaConsumerDTO.getGroupId(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.GROUP_ID_CONFIG))) {
                kafkaConsumer.shutdown();
                it.remove();
            }
        }
        return true;
    }

    /**
     * 校验已经存在
     *
     * @param kafkaConsumerDTO
     * @return
     */
    private boolean alreadyExists(KafkaConsumerDTO kafkaConsumerDTO) {
        Iterator<KafkaConsumer> it = kafkaConsumers.iterator();
        while (it.hasNext()) {
            KafkaConsumer kafkaConsumer = it.next();
            if (Objects.equals(kafkaConsumerDTO.getTopicName(), kafkaConsumer.getTopicName()) && Objects.equals(kafkaConsumerDTO.getBootstrapServers(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG)) && Objects.equals(kafkaConsumerDTO.getGroupId(), kafkaConsumer.getProps().getProperty(CommonClientConfigs.GROUP_ID_CONFIG))) {
                return true;
            }
        }
        return false;
    }
}
```

#### Controller

```java
package com.example.springbootkafkadynamic.example4;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafkaConsumer")
public class KafkaController {

    @Resource
    private KafkaService kafkaService;

    private String TOPIC = "test_topic";

    /**
     *  启动消费
     * @return
     */
    @GetMapping("/start")
    public String start() {
        KafkaConsumerDTO dto = new KafkaConsumerDTO();
        dto.setBootstrapServers("192.168.56.101:9092");
        dto.setGroupId("test_1");
        dto.setTopicName(TOPIC);
        kafkaService.subscribe(dto);
        return "start success";
    }

    /**
     *  停止消费
     * @return
     */
    @GetMapping("/stop")
    public String stop() {
        KafkaConsumerDTO dto = new KafkaConsumerDTO();
        dto.setBootstrapServers("192.168.56.101:9092");
        dto.setGroupId("test_1");
        dto.setTopicName(TOPIC);
        kafkaService.unSubscribe(dto);
        return "stop success";
    }
}
```