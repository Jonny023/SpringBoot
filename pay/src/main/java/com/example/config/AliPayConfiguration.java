package com.example.config;

import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(value = {"ali.pay.open"}, havingValue = "true")
public class AliPayConfiguration {

    @Value("${ali.pay.url}")
    private String url;
    @Value("${ali.pay.notifyUrl}")
    private String aliPayNotifyUrl;
    @Value("${ali.pay.timeoutExpress}")
    private String aliPayTimeOutExpress;
    @Value("${ali.pay.appId}")
    private String aliPayAppId;
    @Value("${ali.pay.charset}")
    private String aliPayCharset;
    @Value("${ali.pay.privateKey}")
    private String aliPayPrivateKey;
    @Value("${ali.pay.signType}")
    private String signType;
    @Value("${ali.pay.format}")
    private String format;
    @Value("${ali.pay.publicKey}")
    private String aliPayPublicKey;
    @Value("${ali.pay.platform.publicKey}")
    private String aliPayPlatformPublicKey;
    @Value("${ali.pay.method}")
    private String method;
    @Value("${ali.pay.version}")
    private String version;
    @Value("${ali.pay.indexPath}")
    private String indexPath;

    @Bean
    public DefaultAlipayClient buildAliPayClient() {
        return new DefaultAlipayClient(
                url
                , aliPayAppId
                , aliPayPrivateKey
                , format
                , aliPayCharset
                , aliPayPublicKey
                , signType
                , "LwInLH6w7BA3kiQAxLaQ+g=="
                , "AES"
        );
    }

}
