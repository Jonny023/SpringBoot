package com.example.springbootopenfeign.controller;

import com.example.springbootopenfeign.service.ApiClient;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

@RestController
public class PostController {

    @Resource
    private ApiClient apiClient;

    @GetMapping()
    public Object get() {
        String url = "http://api.k780.com/?app=weather.history&weaId=1&date=20150720&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
        return apiClient.getData(URI.create(url), new HashMap<>());
    }

    @GetMapping("parse")
    public Object parse() {
        String url = "https://i.news.qq.com/trpc.qqnews_web.pc_base_srv.base_http_proxy/NinjaPageContentSync?pull_urls=news_top_2018";
        Map<String, Object> map = new HashMap<>();
        String jsonStr = apiClient.getData(URI.create(url), map);
        //https://www.cnblogs.com/Durant0420/p/14963310.html
        Object object = JsonPath.read(jsonStr, "$.data[*]");
        if (object instanceof JSONArray) {
            Set<? extends Map.Entry<?, ?>> entries = ((LinkedHashMap<?, ?>) ((JSONArray) object).get(0)).entrySet();
            for (Map.Entry<?, ?> entry : entries) {
                //entry.getValue() instanceof Integer
                //System.out.println(entry.getValue().getClass());
                System.out.println(ofType(entry.getValue()));

            }
        }
        return object;
    }

    private <T> ColumnDataType ofType(T input) {
        if (input instanceof Number) {
            return ColumnDataType.number;
        } else if (input instanceof Date) {
            return ColumnDataType.date;
        } else {
            return ColumnDataType.string;
        }
    }

    public enum ColumnDataType {
        number, string, date
    }
}