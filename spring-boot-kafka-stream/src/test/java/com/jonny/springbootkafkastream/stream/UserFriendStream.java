package com.jonny.springbootkafkastream.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

/**
 * @author: e-lijing6
 * @description: 扁平化处理后写入新的topic
 * @date:created in 2021/4/26 13:29
 * @modificed by:
 */
public class UserFriendStream {

  public static void main(String[] args) {
    Properties prop = new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "UserFriendStream1");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    builder.stream("usr_friend_in").flatMap((k, v) -> {
      List<KeyValue<String, String>> list = new ArrayList<>();
      String[] info = v.toString().split(",");
      if (info.length == 2) {
        String[] friends = info[1].split("\\s+");
        if (info[0].trim().length() > 0) {
          for (String friend : friends) {
            //为了方便测试打印出来
            System.out.println(info[0] + "    " + friend);
            list.add(new KeyValue<String, String>(null, info[0] + "," + friend));
          }
        }
      }
      return list;
    }).to("usr_friend_out");

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
