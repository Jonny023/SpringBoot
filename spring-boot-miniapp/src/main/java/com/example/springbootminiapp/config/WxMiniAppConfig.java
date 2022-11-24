package com.example.springbootminiapp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedissonConfigImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Data
@Configuration
@ConfigurationProperties(prefix = "wx.login")
public class WxMiniAppConfig {

    private String appId;
    private String appSecret;

    private static final String keyPrefix = "miniapp:wx";

    //@Resource
    //private RedissonClient redissonClient;

    /**
     *  单机版
     * @return
     */
    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(appId);
        config.setSecret(appSecret);
        return config;
    }

    /**
     * 分布式版
     * @return
     */
    //@Bean
    //public WxMaConfig wxMaConfig() {
    //    WxMaRedissonConfigImpl config = new WxMaRedissonConfigImpl(redissonClient, keyPrefix);
    //    config.setAppid(appId);
    //    config.setSecret(appSecret);
    //    return config;
    //}

    @Bean
    public WxMaService wxMaService(WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }
}
