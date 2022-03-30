package com.example.springbootopenfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "API-SERVICE", url = "url-placeholder")
public interface ApiClient {

    @GetMapping(value = "")
    String getData(URI uri, @RequestParam Map<String, Object> map);

}