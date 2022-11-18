package com.example.domain.dto;

import com.example.pay.enums.BooleanEnum;
import com.example.pay.enums.PayType;
import lombok.Data;

@Data
public class ShopDTO {

    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 随机码用于回调验证
     */
    private String attach;
    /**
     * 商品总额,分
     */
    private Integer total;

    /**
     * 支付类型
     */
    private PayType payType;

    /**
     * 0-native(二维码)
     * 1-h5
     */
    private BooleanEnum h5;

    /**
     *  ip地址,微信支付h5必传
     */
    private String remoteAddr;
}
