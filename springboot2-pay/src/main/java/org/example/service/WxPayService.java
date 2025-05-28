package org.example.service;

import org.example.domain.common.R;
import org.example.domain.vo.WxPayTradeVO;

import javax.servlet.http.HttpServletRequest;

public interface WxPayService {

    String nativePay(WxPayTradeVO wxPayTradeVO, HttpServletRequest request);

    R payNotify(HttpServletRequest request) throws Exception;
}