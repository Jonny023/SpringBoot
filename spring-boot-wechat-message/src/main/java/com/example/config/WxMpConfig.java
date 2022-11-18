package com.example.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpTemplateMsgServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信公众平台配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.message")
public class WxMpConfig {

	private String appId;
	private String appSecret;
	private String token;
	private String aesKey;

	@Bean
	public WxMpDefaultConfigImpl wxMpDefaultConfigImpl() {
		WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
		config.setAppId(appId);
		config.setSecret(appSecret);
		config.setToken(token);
		config.setAesKey(aesKey);
		return config;
	}

	@Bean
	public WxMpService wxMpService() {
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpDefaultConfigImpl());
		return wxMpService;
	}

}