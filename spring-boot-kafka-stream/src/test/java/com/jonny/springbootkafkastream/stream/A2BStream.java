package com.jonny.springbootkafkastream.stream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

/**
 * @author: Jonny
 * @description: 从一个topic复制到另一个topic
 * @date:created in 2021/4/26 9:07
 * @modificed by:
 */
public class A2BStream {

  // https://blog.csdn.net/weixin_48185778/article/details/111321994
  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-kafka-app");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "10.113.74.45:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    //创建流构造器
    StreamsBuilder builder = new StreamsBuilder();

    //构建好builder，将myStreamIn topic中的数据写入到myStreamOut topic中
    builder.stream("topic_1").to("topic_2");

    final Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology, props);

    final CountDownLatch latch = new CountDownLatch(1);
    Runtime.getRuntime().addShutdownHook(new Thread("topic_1_2_topic_2") {
      @Override
      public void run() {
        streams.close();
        latch.countDown();
      }
    });
    try {
      streams.start();
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.exit(0);
  }
}
