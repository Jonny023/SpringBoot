package org.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.WebUtils;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.example.config.AlipayConfig;
import org.example.domain.vo.AlipayTradeVO;
import org.example.service.AlipayService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service
@CacheConfig(cacheNames = "aliPay")
public class AlipayServiceImpl implements AlipayService {

    @Resource
    private AlipayConfig alipayConfig;

    @Override
    public String payPc(AlipayTradeVO trade) throws Exception {

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());

        // 创建API对应的request(电脑网页版)
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        // 订单完成后返回的页面和异步通知地址
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 填充订单参数
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", trade.getOutTradeNo());
        // 支付金额，最小值0.01元
        bizContent.put("total_amount", trade.getTotalAmount());
        // 订单标题，不可使用特殊符号
        bizContent.put("subject", trade.getSubject());
        // 电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        // PC扫码支付的方式（注释掉不加此参数，pc页面支持扫码和登录账号网页直接支付；设置为0，仅显示二维码，无商品和金额信息；设置1，显示二维码和金额）
        bizContent.put("qr_pay_mode", 1);

        bizContent.put("body", trade.getBody());

        // 扩展信息，按需传入
        JSONObject extendParams = new JSONObject();
        extendParams.put("sys_service_provider_id", alipayConfig.getSysServiceProviderId());
        bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());

        // 调用SDK生成表单, 通过GET方式，口可以获取url
        return alipayClient.pageExecute(request, "GET").getBody();

    }

    @Override
    public String payH5(AlipayTradeVO trade) throws Exception {

        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());

        // 创建API对应的request(手机网页版)
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + trade.getOutTradeNo() + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + trade.getTotalAmount() + "," +
                "    \"subject\":\"" + trade.getSubject() + "\"," +
                "    \"body\":\"" + trade.getBody() + "\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"" + alipayConfig.getSysServiceProviderId() + "\"" +
                "    }" +
                "  }");
        return alipayClient.pageExecute(request, "GET").getBody();
    }

    @Override
    public Map<String, String> toPayTradeQuery(String outOrderNo) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), alipayConfig.getFormat(), alipayConfig.getCharset(), alipayConfig.getPublicKey(), alipayConfig.getSignType());
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outOrderNo);
        request.setBizContent(bizContent.toString());

        // https://opendocs.alipay.com/support/01rays
        WebUtils.setNeedCheckServerTrusted(false);

        AlipayTradeQueryResponse response = alipayClient.execute(request);
        Map<String, String> map = new HashMap<>();
        if (response.isSuccess()) {
            map.put("trade_status", response.getTradeStatus());
            map.put("total_amount", response.getTotalAmount());
            map.put("send_pay_date", DateUtil.format(response.getSendPayDate(), DatePattern.NORM_DATETIME_PATTERN));
            map.put("state", "true");
            return map;
        } else {
            map.put("state", "false");
            return map;
        }
    }
}

