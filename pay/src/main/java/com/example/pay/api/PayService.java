package com.example.pay.api;

import com.example.domain.dto.OrderPayDTO;
import com.example.domain.dto.OrderPayVO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;

import javax.servlet.http.HttpServletRequest;

public interface PayService {

    PcPayDTO pay(OrderPayVO vo);

    /**
     *  创建订单
     */
    String createOrder(HttpServletRequest request);

}
