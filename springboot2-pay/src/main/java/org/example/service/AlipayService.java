package org.example.service;


import org.example.domain.vo.AlipayTradeVO;

import java.util.Map;


public interface AlipayService {

    /**
     * 处理来自PC的交易请求
     *
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String payPc(AlipayTradeVO trade) throws Exception;

    /**
     * 处理来自手机网页的交易请求(h5)
     *
     * @param trade 交易详情
     * @return String
     * @throws Exception 异常
     */
    String payH5(AlipayTradeVO trade) throws Exception;

    /**
     * 交易状态查询
     *
     * @param outOrderNo 业务交易编号（订单号）
     * @return 交易结果
     */
    Map<String, String> toPayTradeQuery(String outOrderNo) throws Exception;

}

