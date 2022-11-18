package com.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("支付订单DTO")
public class OrderPayDTO {

    @ApiModelProperty(value = "微信预支付id，支付宝调用是此字段为orderInfo")
    private String prepayid;

    @ApiModelProperty(value = "微信支付appid")
    private String appid;

    @ApiModelProperty(value = "微信支付partnerid")
    private String partnerid;

    @ApiModelProperty(value = "微信支付package")
    @JsonProperty(value = "package")
    private String _package;

    @ApiModelProperty(value = "微信支付noncestr")
    private String noncestr;

    @ApiModelProperty(value = "微信支付timestamp")
    private String timestamp;

    @ApiModelProperty(value = "微信支付sign")
    private String sign;
}