package com.example.pay;

import com.example.domain.dto.OrderPayDTO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;

public interface Pay {

    /**
     * 创建订单
     *
     * @param param
     * @return
     */
    OrderPayDTO createOrder(ShopDTO param);

    /**
     * 支付
     *
     * @param param
     * @return
     */
    PcPayDTO pay(ShopDTO param);

    /**
     * 通用支付预订单关闭
     * 支付宝传入值为  支付宝平台流水单号
     * 微信传入值为    商户订单号
     */
    boolean closeOrder(String outTradeNo);
}