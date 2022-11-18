package com.example.controller;

import com.example.domain.dto.OrderPayVO;
import com.example.pay.api.PayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PaymentController {

    @Resource
    private PayService payService;

    @PostMapping("createOrder")
    public Object createOrder(HttpServletRequest request) {
        return payService.createOrder(request);
    }

    @PostMapping("pay")
    public Object pay(@RequestBody OrderPayVO vo) {
        return payService.pay(vo);
    }

}