package com.jonny.springbootkafkastream.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KTable;

/**
 * @author: e-lijing6
 * @description: 按键统计
 * @date:created in 2021/4/26 9:30
 * @modificed by:
 */
public class WordCountStream {

  public static void main(String[] args) {
    Properties prop =new Properties();
    prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"word-count-stream");
    prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092"); //zookeeper的地址
    prop.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,2000);  //提交时间设置为2秒
    //prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,""earliest );   //earliest  latest  none  默认latest
    //prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");  //true(自动提交)  false(手动提交)
    prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

    //创建流构造器
    StreamsBuilder builder = new StreamsBuilder();
    KTable<String, Long> count = builder.stream("topic_1") //从kafka中一条一条取数据
        .flatMapValues(                //返回扁平化的数据
            (value) -> {           //返回List集合
              String[] split = value.toString().split("\"");
              List<String> strings = Arrays.asList(split);
              return strings;
            })  //null hello,null world,null hello,null java
        .map((k, v) -> {
          return new KeyValue<String, String>(v,"1");
        }).groupByKey().count();
    count.toStream().foreach((k,v)->{
      //为了测试方便，我们将kv输出到控制台
      System.out.println("key:"+k+"   "+"value:"+v);
    });

    count.toStream().map((x,y)->{
      return new KeyValue<String,String>(x,y.toString());  //注意转成toString类型，我们前面设置的kv的类型都是string类型
    }).to("word_count_output");

    final Topology topo=builder.build();
    final KafkaStreams streams = new KafkaStreams(topo, prop);

    final CountDownLatch latch = new CountDownLatch(1);
    Runtime.getRuntime().addShutdownHook(new Thread("word_count_output_stream"){
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
