package com.example.config;


import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Data
@Configuration
@ConditionalOnProperty(value = {"wechat.pay.open"}, havingValue = "true")
public class WechatPayConfiguration {

    @Value("${wechat.pay.merchantId}")
    private String merchantId;
    @Value("${wechat.pay.appId}")
    private String appId;
    @Value("${wechat.pay.notifyUrl}")
    private String notifyUrl;
    @Value("${wechat.pay.createUrl}")
    private String createUrl;
    @Value("${wechat.pay.h5.createUrl}")
    private String h5CreateUrl;
    @Value("${wechat.pay.native.createUrl}")
    private String nativeCreateUrl;
    @Value("${wechat.pay.timeoutExpress}")
    private int timeoutExpress;
    @Value("${wechat.pay.closeUrlPrefix}")
    private String closeUrlPrefix;
    @Value("${wechat.pay.closeUrlSuffix}")
    private String closeUrlSuffix;

    @Value("${wechat.pay.merchantSerialNumber:0}")
    private String merchantSerialNumber;
    @Value("${wechat.pay.merchantPrivateKey:0}")
    private String merchantPrivateKey;
    @Value("${wechat.pay.apiV3Key:0}")
    private String apiV3Key;

    @Bean("weChartHttpClient")
    public HttpClient createWeChatClient() {
        PrivateKey privateKey = PemUtil.loadPrivateKey(
                new ByteArrayInputStream(merchantPrivateKey.getBytes(StandardCharsets.UTF_8)));
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, privateKey)
                .withValidator(buildWechatPay2Validator());
        return builder.build();
    }

    @Bean
    public WechatPay2Validator buildWechatPay2Validator() {
        PrivateKey privateKey = PemUtil.loadPrivateKey(
                new ByteArrayInputStream(merchantPrivateKey.getBytes(StandardCharsets.UTF_8)));
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, privateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));
        return new WechatPay2Validator(verifier);
    }

    @Bean
    public PrivateKeySigner buildWechatPaySigner() {
        PrivateKey privateKey = PemUtil.loadPrivateKey(
                new ByteArrayInputStream(merchantPrivateKey.getBytes(StandardCharsets.UTF_8)));
        return new PrivateKeySigner(merchantSerialNumber, privateKey);
    }

    @Bean
    public AutoUpdateCertificatesVerifier buildWechatVerifier() {
        PrivateKey privateKey = PemUtil.loadPrivateKey(
                new ByteArrayInputStream(merchantPrivateKey.getBytes(StandardCharsets.UTF_8)));
        return new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(merchantId, new PrivateKeySigner(merchantSerialNumber, privateKey)),
                apiV3Key.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public AesUtil buildAes() {
        return new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
    }
}
