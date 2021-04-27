package com.jonny.springbootkafkastream.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author: Jonny
 * @description: 反序列化
 * @date:created in 2021/4/25 10:57
 * @modificed by:
 */
public class JsonDeserializer<T> implements Deserializer<T> {

  private ObjectMapper objectMapper = new ObjectMapper();
  private Class<T> type;

  /**
   * Default constructor needed by Kafka
   */

  public JsonDeserializer() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
    type = (Class<T>) props.get("JsonPOJOClass");
  }

  @Override
  public T deserialize(String topic, byte[] bytes) {
    if (bytes == null) {
      return null;
    }

    T data;
    try {
      data = objectMapper.readValue(bytes, type);
    } catch (Exception e) {
      throw new SerializationException(e);
    }

    return data;
  }

  @Override
  public void close() {

  }
}
