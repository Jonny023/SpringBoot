package com.jonny.springbootkafkastream.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author: Jonny
 * @description: 序列化
 * @date:created in 2021/4/25 10:57
 * @modificed by:
 */
public class JsonSerializer<T> implements Serializer<T> {

  private ObjectMapper objectMapper = new ObjectMapper();

  // public GenericSerializer(Class<T> pojoClass) {
  public JsonSerializer() {
  }

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
  }

  @Override
  public byte[] serialize(String topic, T data) {
    if (data == null)
      return null;

    try {
      return objectMapper.writeValueAsBytes(data);
    } catch (Exception e) {
      throw new SerializationException("Error serializing JSON message", e);
    }
  }

  @Override
  public void close() {

  }
}