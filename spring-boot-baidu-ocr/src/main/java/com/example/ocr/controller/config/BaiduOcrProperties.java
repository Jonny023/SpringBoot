package com.example.ocr.controller.config;

import com.baidu.aip.ocr.AipOcr;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("baidu.ocr")
public class BaiduOcrProperties {

    private String appId;
    private String apiKey;
    private String secretKey;

    @Bean
    public AipOcr apiOcr() {
        return new AipOcr(appId, apiKey, secretKey);
    }
}