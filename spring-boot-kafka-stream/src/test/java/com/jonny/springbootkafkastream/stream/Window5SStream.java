package com.jonny.springbootkafkastream.stream;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

/**
 * @author: e-lijing6
 * @description: 每隔5秒钟输出一次过去5秒内win_5s_in里的wordcount，结果写入到win_5s_out
 * @date:created in 2021/4/26 11:12
 * @modificed by:
 */
public class Window5SStream {

  public static void main(String[] args) {
    Properties prop = new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "WindowStream_");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 3000);  //提交时间设置为3秒
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, Object> source = builder.stream("win_5s_in");
    KTable<Windowed<String>, Long> countKtable = source.flatMapValues(value -> Arrays
        .asList(value.toString().split("\\s+")))
        .map((x, y) -> {
          return new KeyValue<String, String>(y, "1");
        }).groupByKey()
        //加五秒的窗口(前一个5秒和下一个5秒没有任何交叉) Tumbling Time Window
        .windowedBy(TimeWindows.of(Duration.ofSeconds(5).toMillis()))
        .count();
    countKtable.toStream().foreach((x, y) -> {
      System.out.println("x: " + x.key() + "  y: " + y);
    });

    countKtable.toStream().map((x, y) -> {
      return new KeyValue<String, String>(x.key(), y.toString());
    }).to("win_5s_out");

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
