package com.example.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.example.config.AliPayConfiguration;
import com.example.domain.dto.OrderPayDTO;
import com.example.domain.dto.PcPayDTO;
import com.example.domain.dto.ShopDTO;
import com.example.exctption.PayException;
import com.example.pay.enums.BooleanEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AliPay implements Pay {

    private final Logger log = LoggerFactory.getLogger(AliPay.class);

    @Lazy
    @Resource
    private AliPayConfiguration config;

    @Lazy
    @Resource
    private DefaultAlipayClient defaultAlipayClient;

    @Override
    public OrderPayDTO createOrder(ShopDTO param) {
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody((param.getDescription() != null && param.getDescription().length() > 30) ? param.getDescription().substring(0, 30) : param.getDescription());
        model.setSubject(param.getSubject());
        model.setOutTradeNo(param.getOutTradeNo());
        model.setTimeoutExpress(config.getAliPayTimeOutExpress());
        model.setTotalAmount((param.getTotal() / 100.0) + "");
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setPassbackParams(param.getAttach());
        try {
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            request.setBizModel(model);
            request.setNotifyUrl(config.getAliPayNotifyUrl());
            AlipayTradeAppPayResponse response = this.defaultAlipayClient.sdkExecute(request);
            return OrderPayDTO.builder().prepayid(response.getBody()).build();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("支付宝订单创建失败: ", e);
            throw new PayException("支付宝订单创建失败", e);
        }
    }

    @Override
    public PcPayDTO pay(ShopDTO param) {
        log.info("支付宝支付");
        PcPayDTO orderPayDTO;
        if (param.getH5() == BooleanEnum.TRUE) {
            orderPayDTO = this.wapPay(param.getOutTradeNo(), param.getSubject(), param.getAttach(), param.getTotal());
        } else {
            //native
            orderPayDTO = this.pagePay(param.getOutTradeNo(), param.getSubject(), param.getAttach(), param.getTotal());
        }
        return orderPayDTO;
    }

    /**
     * @param outTradeNo     商户订单号
     * @param subject        订单标题
     * @param passBackParams 随机码用于回调验证
     * @param total          商品总额,分
     * @return
     */
    public PcPayDTO pagePay(String outTradeNo, String subject, String passBackParams, int total) {
        AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
        pagePayModel.setOutTradeNo(outTradeNo);
        pagePayModel.setTotalAmount((total / 100.0) + "");
        pagePayModel.setSubject(subject);
        pagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        pagePayModel.setPassbackParams(passBackParams);
        try {
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setBizModel(pagePayModel);
            request.setNotifyUrl(config.getAliPayNotifyUrl());
            AlipayTradePagePayResponse response = this.defaultAlipayClient.sdkExecute(request);
            return PcPayDTO.builder().payCode(config.getUrl() + "?" + response.getBody()).build();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new PayException("支付宝订单生成失败", e);
        }
    }

    /**
     * @param outTradeNo     商户订单号
     * @param subject        订单标题
     * @param passBackParams 随机码用于回调验证
     * @param total          商品总额,分
     * @return
     */
    public PcPayDTO wapPay(String outTradeNo, String subject, String passBackParams, int total) {
        AlipayTradeWapPayModel wapPayModel = new AlipayTradeWapPayModel();
        wapPayModel.setOutTradeNo(outTradeNo);
        wapPayModel.setTotalAmount((total / 100.0) + "");
        wapPayModel.setSubject(subject);
        wapPayModel.setProductCode("QUICK_WAP_PAY");
        wapPayModel.setQuitUrl(config.getIndexPath());
        wapPayModel.setPassbackParams(passBackParams);
        try {
            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            request.setBizModel(wapPayModel);
            request.setNotifyUrl(config.getAliPayNotifyUrl());
            AlipayTradeWapPayResponse response = this.defaultAlipayClient.sdkExecute(request);
            return PcPayDTO.builder().payCode(config.getUrl() + "?" + response.getBody()).build();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new PayException("支付宝订单创建失败", e);
        }
    }

    @Override
    public boolean closeOrder(String outTradeNo) {
        AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setTradeNo(outTradeNo);
        try {
            AlipayTradeCloseResponse response = defaultAlipayClient.sdkExecute(closeRequest);
            if (response.getTradeNo() != null && response.getTradeNo().equals(outTradeNo)) {
                return true;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new PayException("订单关闭失败", e);
        }
        return false;
    }
}
