package com.example.springbootopenfeign.demo;

import cn.hutool.json.JSONUtil;
import com.jayway.jsonpath.JsonPath;

import java.util.LinkedHashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String jsonStr = "{\"name\": \"zhangsan\", \"data\": \"{}\"}";
        LinkedHashMap<String, String> jsonMap = JSONUtil.toBean(jsonStr, LinkedHashMap.class);
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        Object read = JsonPath.read(jsonStr, "$.data");
        System.out.println(read);

    }
}
