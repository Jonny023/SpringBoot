package com.example.pay;

import com.example.domain.dto.OrderPayDTO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnionPay implements Pay {

    private final Logger log = LoggerFactory.getLogger(UnionPay.class);

    @Override
    public OrderPayDTO createOrder(ShopDTO param) {
        return null;
    }
    
    @Override
    public PcPayDTO pay(ShopDTO param) {
        log.info("银联支付");
        return null;
    }

    @Override
    public boolean closeOrder(String outTradeNo) {
        return false;
    }

}