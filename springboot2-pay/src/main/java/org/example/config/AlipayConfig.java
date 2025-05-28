package org.example.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "pay.alipay.app")
public class AlipayConfig {

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "商户私钥")
    private String privateKey;

    @ApiModelProperty(value = "支付宝公钥")
    private String publicKey;

    @Builder.Default
    @ApiModelProperty(value = "签名方式")
    private String signType = "RSA2";

    @Builder.Default
    @ApiModelProperty(value = "支付宝开放安全地址", hidden = true)
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    @Builder.Default
    @ApiModelProperty(value = "编码", hidden = true)
    private String charset = "utf-8";

    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "订单完成后返回的页面")
    private String returnUrl;

    @Builder.Default
    @ApiModelProperty(value = "类型")
    private String format = "JSON";

    @ApiModelProperty(value = "商户号")
    private String sysServiceProviderId;

}
