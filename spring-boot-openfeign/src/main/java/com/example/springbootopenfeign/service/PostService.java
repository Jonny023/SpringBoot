package com.example.springbootopenfeign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://api.k780.com/", value = "httpService")
public interface PostService {

    @PostMapping("/")
    Object post();
}
