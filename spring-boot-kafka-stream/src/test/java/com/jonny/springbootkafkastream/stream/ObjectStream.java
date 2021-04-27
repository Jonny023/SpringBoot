package com.jonny.springbootkafkastream.stream;

import com.jonny.springbootkafkastream.entity.User;
import com.jonny.springbootkafkastream.serialize.SerdesFactory;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/4/26 16:03
 * @modificed by:
 */
public class ObjectStream {

  public static void main(String[] args) {
    Properties prop = new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-stream-object1");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 2000);  //提交时间设置为2秒
    //prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");   //earliest  latest  none  默认latest
    //prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");  //true(自动提交)  false(手动提交)
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();

    final Serde<User> userSerde = SerdesFactory.serdFrom(User.class);

    KStream<String, User> kStream = builder.stream("user_in", Consumed.with(Serdes.String(), userSerde));

    kStream.foreach((k, v) -> {
      System.out.println(k);
      System.out.println(v);
    });

    final Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology, prop);

    final CountDownLatch latch = new CountDownLatch(1);
    Runtime.getRuntime().addShutdownHook(new Thread("stream") {
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
