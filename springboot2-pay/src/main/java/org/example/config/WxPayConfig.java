package org.example.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.h5.H5Service;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.wxpay.app")
public class WxPayConfig {

    private final ResourceLoader resourceLoader;

    public WxPayConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath;
    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;
    /**
     * 商户APIV3密钥
     */
    private String apiV3Key;
    /**
     * AppId
     */
    private String appId;
    /**
     * 通知url
     */
    private String notifyUrl;

    private Config config;

    @PostConstruct
    public void initConfig() {

        try {
            // 从classpath加载私钥内容
            Resource resource = resourceLoader.getResource(privateKeyPath);
            String privateKeyContent = StreamUtils.copyToString(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8
            );

            // 使用自动更新平台证书的RSA配置
            // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
            config = new RSAAutoCertificateConfig.Builder()
                    .merchantId(merchantId)
                    // .privateKeyFromPath(privateKeyPath)
                    .privateKey(privateKeyContent)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3Key)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Primary
    @Bean
    public H5Service h5Service() {
        return new H5Service.Builder()
                .config(config)
                .build();
    }

    @Primary
    @Bean
    public JsapiService jsapiService() {
        return new JsapiService.Builder()
                .config(config)
                .build();
    }

    @Primary
    @Bean()
    public NativePayService nativePayService() {
        return new NativePayService.Builder()
                .config(config)
                .build();
    }

    @Primary
    @Bean
    public NotificationParser notificationParser() {
        return new NotificationParser((NotificationConfig) config);
    }
}
