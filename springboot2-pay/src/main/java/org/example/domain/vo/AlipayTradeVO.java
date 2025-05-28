package org.example.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author admin
 */
@Data
public class AlipayTradeVO {

    @ApiModelProperty("商户订单号（业务平台）")
    private String outTradeNo;

    @ApiModelProperty("商户订单号（支付平台）")
    private String tradeNo;

    @ApiModelProperty("支付金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("订单标题")
    private String subject;

    @ApiModelProperty("交易的简要描述")
    private String body;
}