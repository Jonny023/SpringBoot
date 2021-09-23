package com.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description: Jackson转换JSON工具类
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.registerModule(new JavaTimeModule());
    }

    public static String toJSONString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws JsonProcessingException {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return mapper.readValue(json, clazz);
    }

    public static <T> List toArray(String json, Class<T> clazz) throws JsonProcessingException {
        try {
            if (!StringUtils.hasText(json)) {
                return null;
            }
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("decode(String, JsonTypeReference<T>)", e);
            throw e;
        }
    }

    public static Map<String, Object> toObject(String json) throws JsonProcessingException {
        return toObject(json, Map.class);
    }

    public static List<Map<String, Object>> toArray(String json) throws JsonProcessingException {
        return toArray(json, Map.class);
    }

    public static <T> T toClass(String json, Class<T> clazz) {
        try {
            if (Optional.ofNullable(json).orElse("").trim().isEmpty()) {
                return null;
            }
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("cast to class failed {}", e);
        }
        return null;
    }

    public static boolean isJson(String data) {
        try {
            mapper.readTree(data);
            return true;
        } catch (JsonProcessingException e) {
            log.warn("{} is not json", data);
            return false;
        }
    }

}
