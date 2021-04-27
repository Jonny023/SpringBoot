package com.jonny.springbootkafkastream.stream;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

/**
 * @author: Jonny
 * @description: 将topic的多列扁平化处理写入新的topic
 * @date:created in 2021/4/26 13:40
 * @modificed by:
 */
public class EventAttendStream {

  public static void main(String[] args) {
    Properties prop =new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"UserFriendStream1");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<Object, Object> ear = builder.stream("event_in");
    KStream<String, String> eventStream = ear.flatMap((k, v) -> {
      System.out.println(k + " " + v);
      String[] split = v.toString().split(",");
      ArrayList<KeyValue<String, String>> list = new ArrayList<>();
      if (split.length >= 2 && split[1].trim().length() > 0) {
        String[] yes = split[1].split("\\s+");
        for (String y : yes) {
          list.add(new KeyValue<String, String>(null, split[0] + "," + y + ",yes"));
        }
      }

      if (split.length >= 3 && split[2].trim().length() > 0) {
        String[] maybe = split[2].split("\\s+");
        for (String mb : maybe) {
          list.add(new KeyValue<String, String>(null, split[0] + "," + mb + ",maybe"));
        }
      }

      if (split.length >= 4 && split[3].trim().length() > 0) {
        String[] invited = split[3].split("\\s+");
        for (String inv : invited) {
          list.add(new KeyValue<String, String>(null, split[0] + "," + inv + ",invited"));
        }
      }
      if (split.length >= 5 && split[4].trim().length() > 0) {
        String[] no = split[4].split("\\s+");
        for (String n : no) {
          list.add(new KeyValue<String, String>(null, split[0] + "," + no + ",no"));
        }
      }

      return list;
    });

    eventStream.to("event_out");

    final Topology topo=builder.build();
    final KafkaStreams streams = new KafkaStreams(topo, prop);

    final CountDownLatch latch = new CountDownLatch(1);
    Runtime.getRuntime().addShutdownHook(new Thread("stream"){
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
