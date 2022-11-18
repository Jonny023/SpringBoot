package com.jonny.springbootkafkastream.serialize;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.rocksdb.Statistics;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/4/26 16:38
 * @modificed by:
 */
public class SerdesFactory {

  /**
   * 序列化和反序列化能用方法，
   */
  public static <T> Serde<T> serdFrom(Class<T> pojoClass) {
    Map<String, Object> serdeProps = new HashMap<>();
    final Serializer<Statistics> statisticsSerializer = new JsonSerializer<>();
    serdeProps.put("JsonPOJOClass", pojoClass);
    statisticsSerializer.configure(serdeProps, false);

    final Deserializer<Statistics> statisticsDeserializer = new JsonDeserializer<>();
    serdeProps.put("JsonPOJOClass", pojoClass);
    statisticsDeserializer.configure(serdeProps, false);

    // return Serdes.serdeFrom(new GenericSerializer<T>(pojoClass), new GenericDeserializer<T>(pojoClass));
    return (Serde<T>) Serdes.serdeFrom(statisticsSerializer, statisticsDeserializer);
  }
}
