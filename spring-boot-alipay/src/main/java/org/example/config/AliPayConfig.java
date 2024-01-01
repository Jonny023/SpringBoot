package org.example.config;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
import javax.annotation.PostConstruct;
 
/**
 * @author Jonny
 * @date 2024-01-01 14:21:17
 * @description: 支付宝配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

   private String appId;
   private String appPrivateKey;
   private String alipayPublicKey;
   private String notifyUrl;

   @PostConstruct
   public void init() {
      Config config = new Config();
      config.protocol = "https";
      //config.gatewayHost = "openapi.alipaydev.com";
      config.gatewayHost = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
      config.signType = "RSA2";
      config.appId = this.appId;
      config.merchantPrivateKey = this.appPrivateKey;
      config.alipayPublicKey = this.alipayPublicKey;
      config.notifyUrl = this.notifyUrl;
      Factory.setOptions(config);
   }
}