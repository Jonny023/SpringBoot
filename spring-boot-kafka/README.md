# Kafka

* pom.xml

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

* yaml

```yaml
spring:
  kafka:
    bootstrap-servers: 10.113.74.113:9092
    producer:
      retries: 0
      batch-size: 10240
      buffer-memory: 33554432
      properties:
        linger.ms: 1000
        request.timeout.ms: 2000
        max.in.flight.requests.per.connection: 10
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: 1
    consumer:
      # 自动提交的时间间隔 在spring boot 2.X 版本中这里采用的是值的类型为Duration 需要符合特定的格式，如1S,1M,2H,5D
      #      auto-commit-interval: 1S
      # 该属性指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下该作何处理：
      # latest（默认值）在偏移量无效的情况下，消费者将从最新的记录开始读取数据（在消费者启动之后生成的记录）
      # earliest ：在偏移量无效的情况下，消费者将从起始位置读取分区的记录
      auto-offset-reset: latest
      # 是否自动提交偏移量，默认值是true,为了避免出现重复数据和数据丢失，可以把它设置为false,然后手动提交偏移量
      enable-auto-commit: false
      max-poll-records: 5000
      #      max-poll-interval: 15000
      #      max-partition-fetch-bytes: 15728640
      # 键的反序列化方式
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      # 值的反序列化方式
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    listener:
      type: batch
      # 在侦听器容器中运行的线程数。
      concurrency: 4
      #手动提交ack，每调用一次就提交一次
      ack-mode: manual_immediate
      #按照1秒的固定频率消费数据，减小服务压力
      idle-between-polls: 1s
```

* 启用kafka

```java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringBootKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootKafkaApplication.class, args);
    }

}
```

* 注入KafkaTemplate就能使用

```java
@Resource
private KafkaTemplate<String, String> kafkaTemplate;
```