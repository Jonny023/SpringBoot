package com.jonny.springbootkafkastream.stream;

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

/**
 * @author: e-lijing6
 * @description: 求和
 * @date:created in 2021/4/26 9:37
 * @modificed by:
 */
public class SumStream {

//  static {
//    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//    Logger root = loggerContext.getLogger("root");
//    root.setLevel(Level.ERROR);
//  }

  public static void main(String[] args) {
    Properties prop =new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"sum-stream_1");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,2000);  //提交时间设置为2秒
    //prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");   //earliest  latest  none  默认latest
    //prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");  //true(自动提交)  false(手动提交)
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

    //创建流构造器
    StreamsBuilder builder = new StreamsBuilder();
    KStream<Object, Object> source = builder.stream("sum_input");
    KTable<String, String> total = source.map((key, value) ->
        new KeyValue<String, String>("sum", value.toString())
    ).groupByKey().reduce((x, y) -> {
      System.out.println("x: " + x + "    " + "y: "+y);
      Integer sum = Integer.valueOf(x) + Integer.valueOf(y);
      System.out.println("sum: "+sum);
      return sum.toString();
    });
    total.toStream().to("sum_out");

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
